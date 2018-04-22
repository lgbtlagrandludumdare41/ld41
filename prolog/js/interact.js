var mypen = null;

window.addEventListener('load', function() {
	console.log("loaded");
  const inputbox = document.getElementById('inputbox');
  inputbox.focus();
  inputbox.addEventListener("keyup", function(ev) {
  	   if(ev.key == "Enter") {
  	   	console.log("saw return");
  	      mypen.ask("game_turn('" + encodeURI(inputbox.value) + "', ResponseText)", [])
  	   } else {
  	   	console.log(inputbox.innerHTML);
  	      inputbox.value = inputbox.value.toUpperCase();
  	   }
  });
  mypen = new Pengine( {
  	ask: "create_game(ResponseText)",
  	destroy: false,
  	oncreate: function(){
  		console.log("pengine created")
  	},
  	onsuccess: function() {
  		console.log("pengine responds\n");
  		console.log(this);
  		if(this.data.length > 0 && this.data[0]["ResponseText"] != undefined) {
  			 $("<code>&#x25B6;" + inputbox.value + "</code>").appendTo("#codeliketext");
  			 $("<code>" + this.data[0]["ResponseText"] + "</code>").appendTo("#codeliketext");
  			 inputbox.value = ""
  		}
  		if(this.more) {
  		    mypen.next();
  		}
  	}
  });
});
