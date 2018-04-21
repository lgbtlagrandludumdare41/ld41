var mypen = null;

window.addEventListener('load', function() {
	console.log("loaded");
	
  const inputbox = document.getElementById('inputbox');
  inputbox.focus();
  inputbox.addEventListener("keypress", function(ev) {
  	console.log(ev);
  	   if(ev.key == "Enter") {
  	   	console.log("saw return");
  	      mypen.ask("create_game", [])
  	   }
  });
  mypen = new Pengine( {
  	ask: "create_game",
  	destroy: false,
  	oncreate: function(){
  		console.log("pengine created")
  	},
  	onsuccess: function() {
  		console.log("pengine responds\n");
  		console.log(this);
  		if(this.more) {
  		    mypen.next();
  		}
  	}
  });
});
