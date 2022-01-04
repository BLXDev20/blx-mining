var  vm = new Vue({
   el: '#app',
   data: function() {
      return  {
       connected: false, 
       wallet: {},
       mining: {
        hash: "",
		time: {
			day: "0",
			hour: "0",
			min: "0",
			sec: "0",
			msec: "0"
		}
       },
       showWalletDetails: true,
       systemMsg: "",
		state: "init",
		errors: {
			address: "",
			amount: "",
			privateKey: ""
		},
		send: {
			address: "",
			amount: "",
			privateKey: ""
		},
		showError: false,
		showLoading: false
      }
    },
   methods: {
	formatDate: function(time){
		return new Date(time).toLocaleString();
	},
	
	formatBalance: function(num, dec){
		if(num != undefined) {
			return num.toFixed(dec); 
		}  
		
		return num;
	},
	
	pad: function(val) {
	    return val > 9 ? val : "0" + val;
	},
	initHistory: function(){
		this.state = 'history';
		this.getMyWallet();
	},
	
	toHHMMSS: function(ms){
		//const day = Math.floor(ms / (24*60*60*1000));
		 // 1- Convert to seconds:
	  //  let seconds = ms / 1000;
	    // 2- Extract hours:
	   // const hours = parseInt( seconds / 3600 ); // 3,600 seconds in 1 hour
	   // seconds = seconds % 3600; // seconds remaining after extracting hours
	    // 3- Extract minutes:
	  //  const minutes = parseInt( seconds / 60 ); // 60 seconds in 1 minute
	    // 4- Keep only seconds not extracted to minutes:
	 //   seconds = seconds % 60; 
		//ms = 110571000;
		
		const days = Math.floor(ms / (24*60*60*1000));
		  const daysms = ms % (24*60*60*1000);
		  const hours = Math.floor(daysms / (60*60*1000));
		  const hoursms = ms % (60*60*1000);
		  const minutes = Math.floor(hoursms / (60*1000));
		  const minutesms = ms % (60*1000);
		  const sec = Math.floor(minutesms / 1000);

		//console.log(hours + ":" + minutes + ":" + seconds);
		this.mining.time.day = days;
		this.mining.time.hour = hours;
		this.mining.time.min = minutes;
		this.mining.time.sec = sec.toFixed(0);
	},
	connect() {
        this.socket = new SockJS("/track");
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.debug = () => {};

        this.stompClient.connect(
          {},
          frame => {
            this.connected = true;
           
            this.stompClient.subscribe("/topic/mine", tick => {
              this.mining.hash  = tick.body;
              //console.log(tick.body) 

				let sessionInSeconds = tick.body.split('|')[1];
				this.toHHMMSS(sessionInSeconds);

            });

            this.stompClient.subscribe("/topic/block/rewarded", tick => {
              
              this.getMyWallet();
 
            });
          },
          error => { 
            //location.reload();
            this.connected = false;
          }
        );
      },
      
      getMyWallet: function(){
        axios.get('/api/myaddress')
            .then((response) => { 
              this.wallet = response.data;

                this.wallet.history = this.wallet.history.reverse();

				this.wallet.history.sort(function(a, b) {
				    return parseFloat(b.input.timeStamp) - parseFloat(a.input.timeStamp);
				});

				let totalReceived = 0;
				let totalSent = 0;
				this.wallet.history.forEach((e,i) => {
					
					if(e.senderWallet != null) {
					//	console.log(e);
					
						if(e.senderWallet.publicKey == this.wallet.publicKey) {
							totalSent += e.amount;
						} else {
							totalReceived += e.amount;
						}
					}
					
					
				});
				
				this.wallet.miningBalance = this.wallet.balance - (totalReceived - totalSent);


            }).catch((err) => {
                if(err) {
                  this.showWalletDetails = false;
                  this.systemMsg = "You are not connected to the Cloudchain Network. Please check your instance and reload the page to start mining"
                }

            })
      },
   },

	watch: {

      'send.address': function(newValue, oldValue) {
          if(newValue !== oldValue) {  

            this.errors.address = ""; 
          }
      },

	 'send.amount': function(newValue, oldValue) {
	          if(newValue !== oldValue) {  
	
	            this.errors.amount = ""; 
	          }
	      },
	
	 'send.privateKey': function(newValue, oldValue) {
	          if(newValue !== oldValue) {  
	
	            this.errors.privateKey = ""; 
	          }
	      },
    },
   
   beforeMount(){ 
	
	//console.log('Initially ' + (window.navigator.onLine ? 'on' : 'off') + 'line');

	//window.addEventListener('online', () => console.log('Became online'));
	window.addEventListener('offline', () => {
		location.reload();
		
	});

		let msec = 0; 
	setInterval(() => { 
	
		if(this.mining.hash.substr(0, this.mining.hash.indexOf('|')) != "") {
			this.mining.time.msec = ++msec % 60;
		}
		

    }, 50);

	 this.connect();
     this.getMyWallet();
	}
    
})
