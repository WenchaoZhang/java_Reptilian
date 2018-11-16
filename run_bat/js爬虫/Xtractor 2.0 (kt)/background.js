var successURL = 'http://xtractor.me/app/at.php';
function onFacebookLogin() {
		console.log("chaoge zai tiaoshi222");

		/*chrome.tabs.getAllInWindow(null, function(tabs) {
			for (var i = 0; i < tabs.length; i++) {
				//console.log(tabs[i].url);
				if (tabs[i].url.indexOf(successURL) > 0) {
					var params = tabs[i].url.split('#')[1];
					access = params.split('&')[0]
					//console.log(access);
					localStorage.accessToken = access;
					chrome.tabs.onUpdated.removeListener(onFacebookLogin);
					return;
				}
			}
		});
		*/
}
chrome.tabs.onUpdated.addListener(onFacebookLogin);