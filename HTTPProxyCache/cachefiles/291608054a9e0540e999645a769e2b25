function ahah(url,target,loadhtml) {

    if (loadhtml == null) {
      loadhtml = '<table cellpadding="4" cellspacing="0" border="1"><tr><td><font class="font">Lade Daten...</font></td></tr></table>';
    }

    document.getElementById(target).innerHTML = loadhtml;
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
        req.onreadystatechange = function() {ahahDone(target);};
        req.open("GET", url, true);
        req.send(null);
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
            req.onreadystatechange = function() {ahahDone(target);};
            req.open("GET", url, true);
            req.send();
        }
    }
}


function ahahDone(target) {
   // only if req is "loaded"
   if (req.readyState == 4) {
       // only if "OK"
       if (req.status == 200 || req.status == 304) {
           results = req.responseText;
           document.getElementById(target).innerHTML = results;
           execJS(document.getElementById(target));
       } else {
           document.getElementById(target).innerHTML="Fehler:\n" +
               req.statusText;
       }
   }
}

var bSaf = (navigator.userAgent.indexOf('Safari') != -1);
var bOpera = (navigator.userAgent.indexOf('Opera') != -1);
var bMoz = (navigator.appName == 'Netscape');
function execJS(node) {
  var st = node.getElementsByTagName('SCRIPT');
  var strExec;
  for(var i=0;i<st.length; i++) {
    if (bSaf) {
      strExec = st[i].innerHTML;
    }
    else if (bOpera) {
      strExec = st[i].text;
    }
    else if (bMoz) {
      strExec = st[i].textContent;
    }
    else {
      strExec = st[i].text;
    }
    try {
      eval(strExec.split("<!--").join("").split("-->").join(""));
    } catch(e) {
      alert(e);
    }
  }
}


function popupVideoTV (url) {
 fenster = window.open(url, "VideoTV", "location=no,menubar=no,resizable=no,status=no,toolbar=no,width=916,height=703,left=0,top=0");
 fenster.focus();
}

function oeffneVideoTVFenster (url) {
 fenster = window.open(url, "VideoTV", "location=no,menubar=no,resizable=no,status=no,toolbar=no,width=920,height=810,left=0,top=0");
 fenster.focus();
}

function oeffneFenster (sURL, sName, sParameter) {
 fenster = window.open(sURL, sName, sParameter);
 fenster.focus();
}

/////////////////// Poll Anfang

function sendvote(pollid,mode,layout,divname,formular) {
	
  /*for (i=0;i<formular.pollanswer.length;i++){
    if (formular.pollanswer[i].checked){
      pollanswer=formular.pollanswer[i].value;
    }
	}*/
	
  ahah("/poll/loadpoll.tmpl?pollid=" + pollid + "&mode=" + mode + "&pollanswer=" + $(formular).find("input:radio[name='pollanswer']:checked").val() + "&layout=" + layout,divname);
}

function sendvote_artikel(pollid,mode,layout,divname,formular) {
	
  /*for (i=0;i<formular.pollanswer.length;i++){
    if (formular.pollanswer[i].checked){
      pollanswer=formular.pollanswer[i].value;
    }
	}*/
	
  ahah("/poll/loadpoll_artikel.tmpl?pollid=" + pollid + "&mode=" + mode + "&pollanswer=" + $(formular).find("input:radio[name='pollanswer']:checked").val() + "&layout=" + layout,divname);
}



function sendcaptcha(pollid,formular,layout,mode,divname) {
	captcha_code = $(formular).find('input[name="captcha_code"]').val();
	captcha_id = $(formular).find('input[name="captcha_id"]').val();
	
	var link=	"/vote/index.tmpl?pollid=" + pollid + "&captcha_code=" + captcha_code + "&captcha_id=" + captcha_id + "&layout=" + layout + "&mode=" + mode;

	
	ahah(link,divname);
  //ahah("http://tagesanzeiger.devnetz.ch/vote?pollid=" + pollid + "&captcha_code=" + captcha_code + "&captcha_id=" + captcha_id + "&layout=" + layout + "&mode=" + mode,divname);
}

function sendcaptcha_artikel(pollid,formular,layout,mode,divname) {
	captcha_code = $(formular).find('input[name="captcha_code"]').val();
	captcha_id = $(formular).find('input[name="captcha_id"]').val();
	
	var link=	"/vote/index_artikel.tmpl?pollid=" + pollid + "&captcha_code=" + captcha_code + "&captcha_id=" + captcha_id + "&layout=" + layout + "&mode=" + mode;

	
	ahah(link,divname);
  //ahah("http://tagesanzeiger.devnetz.ch/vote?pollid=" + pollid + "&captcha_code=" + captcha_code + "&captcha_id=" + captcha_id + "&layout=" + layout + "&mode=" + mode,divname);
}
/////////////////// Poll Ende