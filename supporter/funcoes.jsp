<%@ page import="br.com.manchestercity.automacao.*, br.com.manchestercity.SystemConfig"%>
<%
	boolean termo = true;
	User user = User.getSession(request, response, false, true, "USER");

	if (user == null) {
		user = new User(request);
		termo = false;
	}

	Database lcdb = new Database(user);
	Utils.noCacheStatic(response, request);
%>
<!--
var charEspecial = [{val:"a",let:"·‡„‚‰"},{val:"e",let:"ÈËÍÎ"},{val:"i",let:"ÌÏÓÔ"},{val:"o",let:"ÛÚıÙˆ"},{val:"u",let:"˙˘˚¸"},{val:"c",let:"Á"},{val:"A",let:"¡¿√¬ƒ"},{val:"E",let:"…» À"},{val:"I",let:"ÕÃŒœ"},{val:"O",let:"”“’‘÷"},{val:"U",let:"⁄Ÿ€‹"},{val:"C",let:"«"}];
function replaceCharEspecial(str) {
  var regex;
  var returnString = str;
  for (var i=0; i<charEspecial.length; i++) {
    regex = new RegExp("["+charEspecial[i].let+"]", "g");
    returnString = returnString.replace(regex, charEspecial[i].val);
    regex = null;
  }
  return returnString;
}
var $sidebarItem;
var objSelecionado = null;
        var mouseOffset = null;
        function addEvent(obj, evType, fn) {

        if (typeof obj == 'string') {
          if (null == (obj = document.getElementById(obj))) {
           throw new Error('Elemento HTML n„o encontrado. N„o foi possÌvel adicionar o evento.');
          }
        }
        if (obj.attachEvent) {
         return obj.attachEvent(('on'+ evType), fn);
         } else if (obj.addEventListener) {
          return obj.addEventListener(evType, fn, true);
        } else {
          throw new Error('Seu browser n„o suporta adiÁ„o de eventos.');
        }
        }
        function mouseCoords(ev){    
            if(typeof(ev.pageX)!=='undefined'){
              return {x:ev.pageX, y:ev.pageY};
           }else{
               return {
                 x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
                y:ev.clientY + document.body.scrollTop  - document.body.clientTop
              };
          }
        }
         function getPosition(e, ev){
           var ev = ev || window.event;
           if(e.constructor==String){ e = document.getElementById(e);}
           var left = 0, top  = 0;    
           var coords = mouseCoords(ev);    

           while (e.offsetParent){
             left += e.offsetLeft;
            top  += e.offsetTop;
            e     = e.offsetParent;
          }
          left += e.offsetLeft;
           top  += e.offsetTop;
           return {x: coords.x - left, y: coords.y - top};
        }

        function dragdrop(local_click, caixa_movida) {
        //local_click indica quem È o elemento que quando clicado e arrastado, move o caixa_movida
           if(local_click.constructor==String){ local_click = document.getElementById(local_click);}
           if(caixa_movida.constructor==String){ caixa_movida = document.getElementById(caixa_movida);}
          
          local_click.style.cursor = 'move';
          if(!caixa_movida.style.position || caixa_movida.style.position=='static'){
              caixa_movida.style.position='relative'
          }
          local_click.onmousedown = function(ev) {
              objSelecionado = caixa_movida;        
              mouseOffset = getPosition(objSelecionado, ev);
          };
          document.onmouseup = function() {
              objSelecionado = null;
           }
          document.onmousemove = function(ev) {
              if (objSelecionado) {
                  var ev = ev || window.event;
                  var mousePos = mouseCoords(ev);
                  var pai = objSelecionado.parentNode;
                  objSelecionado.style.left = (mousePos.x - mouseOffset.x - pai.offsetLeft) + 'px';
                  objSelecionado.style.top = (mousePos.y - mouseOffset.y - pai.offsetTop) + 'px';
                  objSelecionado.style.margin = '0px';
                  return false;
              }
          }
        }


function verificaCheckboxByNameFromPortal(pValue) {
  var element = document.getElementsByName(pValue);
  for (var i=0; i<element.length; i++) {
    if(trim(element[i].value).length > 0 ){
      return true;
    }
  }
  return false;
}

function AjaxUpload(form,form_action,funcao_retorno){
  //testando se passou o ID ou o objeto mesmo
  form = (typeof(form)=="string" ? document.getElementById(form) : form);

  var erro="";
  if(form==null || typeof(form)=="undefined"){
    erro += "O form passado no primeiro par‚metro n„o existe na p·gina.\n";
  }else if(form.nodeName!="FORM"){
    erro += "O form passado no primeiro par‚metro da funÁ„o n„o È um form.\n";
  }
  if(erro.length>0) {
  alert("Erro ao chamar a funÁ„o AjaxUpload:\n" + erro);
    return;
  }

  //criando o iframe
  var iframe = document.createElement("iframe");
  iframe.setAttribute("id"    ,"upload-temp");
  iframe.setAttribute("name"  ,"upload-temp");
  iframe.setAttribute("width" ,"0");
  iframe.setAttribute("height","0");
  iframe.setAttribute("border","0");
  iframe.setAttribute("style" ,"width: 0; height: 0; border: none;");

  //adicionando ao documento
  form.parentNode.appendChild(iframe);
  window.frames['upload-temp'].name="upload-temp"; //ie sucks

  //adicionando o evento ao carregar
  var carregou = function() {
    if (document.getElementById('upload-temp').detachEvent ) {
      document.getElementById('upload-temp').detachEvent( 'onload', carregou );
    }else {
      document.getElementById('upload-temp').removeEventListener( 'load', carregou, false );
    }
    var cross = "javascript: window.parent." + funcao_retorno + "(document.body.innerHTML); void(0); ";

    document.getElementById('upload-temp').src = cross;
    //deleta o iframe
    setTimeout(function(){ try{ document.getElementById('upload-temp').parentNode.removeChild(document.getElementById('upload-temp')); }catch(e){} }, 250);
  }
  if (document.getElementById('upload-temp').addEventListener)
    document.getElementById('upload-temp').addEventListener('load', carregou, true)
  if (document.getElementById('upload-temp').attachEvent)
    document.getElementById('upload-temp').attachEvent("onload", carregou)

  //setando propriedades do form
  form.setAttribute("target"  ,"upload-temp");
  form.setAttribute("action"  ,form_action);
  form.setAttribute("method"  ,"post");
  form.setAttribute("enctype" ,"multipart/form-data");
  form.setAttribute("encoding","multipart/form-data");
  //submetendo
  form.submit();
}

var ieBlink = (document.all)?true:false;
function doBlink(){
	if(ieBlink){
		obj = document.getElementsByTagName('BLINK');
		for(i=0;i<obj.length;i++){
			tag=obj[i];
			tag.style.visibility=(tag.style.visibility=='hidden')?'visible':'hidden';
		}
	}
}

if(ieBlink){setInterval('doBlink()',450)}

function changeLanguage(idi){
      var strUrl = "ajax?acao=muda_idioma&CD_IDI="+idi;
      makeRequest('muda_idioma',strUrl,null,returnAjaxMudaIdioma);
    }
    function returnAjaxMudaIdioma(varRequest){
      var lStr = varRequest.responseText;
      if(lStr.length > 0){
        eval(lStr);
      }
}

function callSimpleRequest(url,isSycn){
  if(isSycn){
  	makeRequestSync('_SimpleRequest',url,null,returnAjaxSimpleRequest);
  }else{
  	makeRequest('_SimpleRequest',url,null,returnAjaxSimpleRequest);
  }
}
function returnAjaxSimpleRequest(varRequest){
  var lStr = varRequest.responseText;
  if(lStr.length > 0){
    try{ eval(lStr); } catch(e){alert(e.message);}
  }
}

function retiraMascaraCPFCNPJ(obj){
   var str = "";
   str = replaceAll(obj.value, ".", "");
   str = replaceAll(str, "-", "");
   str = replaceAll(str, "/", "");

   return str;
}

//onKeyUp='integerMaskWithSignal(this, 5, false);' onBlur='integerMaskWithSignal(this, 5, false);'
/*function integerMaskWithSignal(obj, length, noSignal){
  var lSinal = (noSignal?'':(obj.value.indexOf('-') > -1?'-':''));
  var lValue = obj.value.replace(/[^0-9]/g, '').replace(/^0+/, '');
  if(lValue.length > length){
    lValue = lValue.substring(0, length);
  }
  obj.value = lSinal + lValue;
}*/



function getMousePosition(e) {
	var posx = 0;
	var posy = 0;
	if (!e) var e = window.event || window.Event;
	if (e.pageX || e.pageY) {
		posx = e.pageX;
		posy = e.pageY;
	}
	else if (e.clientX || e.clientY) {
		posx = e.clientX + document.body.scrollLeft
			+ document.documentElement.scrollLeft;
		posy = e.clientY + document.body.scrollTop
			+ document.documentElement.scrollTop;
	}
	// posx and posy contain the mouse position relative to the document
	// Do something with this information
      return { top: posy, left: posx };
}

function getFrameDocument(id)
{
  var oIframe = document.getElementById(id);
  var oDoc = (oIframe.contentWindow || oIframe.contentDocument);
  if (oDoc.document) oDoc = oDoc.document;
  return oDoc;
}


//onKeyUp='ForceCapsLock(this);'
function ForceCapsLock(obj) {
  var lValue = obj.value.replace(/[a-z]/g, '');
  obj.value = lValue;
}

//onKeyUp='CPFCNPJMask(this);' onBlur='CPFCNPJMask(this);'
function CPFCNPJMask(obj){
  var lValue = obj.value.replace(/[^0-9]/g, '');
  if(lValue.length > 3 && lValue.length < 7){
    obj.value = lValue.substring(0, 3) + '.' + lValue.substring(3, lValue.length);
  }else if(lValue.length > 6 && lValue.length < 10){
    obj.value = lValue.substring(0, 3) + '.' + lValue.substring(3, 6) + '.' + lValue.substring(6, lValue.length);
  }else if(lValue.length > 9 && lValue.length < 12){
    obj.value = lValue.substring(0, 3) + '.' + lValue.substring(3, 6) + '.' + lValue.substring(6, 9) + '-' + lValue.substring(9, lValue.length);
  }else if(lValue.length > 11 && lValue.length < 13){
    obj.value = lValue.substring(0, 2) + '.' + lValue.substring(2, 5) + '.' + lValue.substring(5, 8) + '/' + lValue.substring(8, lValue.length);
  }else if(lValue.length > 11 && lValue.length < 14){
    obj.value = lValue.substring(0, 2) + '.' + lValue.substring(2, 5) + '.' + lValue.substring(5, 8) + '/' + lValue.substring(8, 12) + '-' + lValue.substring(12, lValue.length);
  }else if(lValue.length > 13 && lValue.length < 15){
    obj.value = lValue.substring(0, 2) + '.' + lValue.substring(2, 5) + '.' + lValue.substring(5, 8) + '/' + lValue.substring(8, 12) + '-' + lValue.substring(12, 14);
  }else if(lValue.length > 14){
    obj.value = lValue.substring(0, 3) + '.' + lValue.substring(3, 6) + '.' + lValue.substring(6, 9) + '/' + lValue.substring(9, 13) + '-' + lValue.substring(13, 15);
  }else{
    obj.value = lValue;
  }
}


//onKeyUp='CPFMask(this);' onBlur='CPFMask(this);'
function CPFMask(obj){
  var lValue = obj.value.replace(/[^0-9]/g, '');

  if(lValue.length < 4){
    obj.value = lValue;
  }else if(lValue.length > 3 && lValue.length < 7){
    obj.value = lValue.substring(0, 3) + '.' + lValue.substring(3, lValue.length);
  }else if(lValue.length > 6 && lValue.length < 10){
    obj.value = lValue.substring(0, 3) + '.' + lValue.substring(3, 6) + '.' + lValue.substring(6, lValue.length);
  }else if(lValue.length > 9 && lValue.length < 12){
    obj.value = lValue.substring(0, 3) + '.' + lValue.substring(3, 6) + '.' + lValue.substring(6, 9) + '-' + lValue.substring(9, 11);
  }else{
    obj.value = "";
    obj.value = lValue.substring(0, 3) + '.' + lValue.substring(3, 6) + '.' + lValue.substring(6, 9) + '-' + lValue.substring(9, 11);
  }
}

//onKeyUp='CNPJMask(this);' onBlur='CNPJMask(this);'
function CNPJMask(obj){
  var lValue = obj.value.replace(/[^0-9]/g, '');

  if(lValue.length < 3){
    obj.value = lValue;
  }else if(lValue.length > 2 && lValue.length < 6){
    obj.value = lValue.substring(0, 2) + '.' + lValue.substring(2, lValue.length);
  }else if(lValue.length > 5 && lValue.length < 9){
    obj.value = lValue.substring(0, 2) + '.' + lValue.substring(2, 5) + '.' + lValue.substring(5, lValue.length);
  }else if(lValue.length > 8 && lValue.length < 13){
    obj.value = lValue.substring(0, 2) + '.' + lValue.substring(2, 5) + '.' + lValue.substring(5, 8) + '/' + lValue.substring(8, lValue.length);
  }else if(lValue.length > 12 && lValue.length < 14){
    obj.value = lValue.substring(0, 2) + '.' + lValue.substring(2, 5) + '.' + lValue.substring(5, 8) + '/' + lValue.substring(8, 12) + '-' + lValue.substring(12, 14);
  }else{
    obj.value = "";
    obj.value = lValue.substring(0, 2) + '.' + lValue.substring(2, 5) + '.' + lValue.substring(5, 8) + '/' + lValue.substring(8, 12) + '-' + lValue.substring(12, 14);
  }
}

// JavaScript Document
var mDateFormat = "<%=user.getUserDateFormat()%>";


function validateDateFormat(tipo,fields){
	var valor = fields.value;
	if(trim(valor).length==0)return true;
	var strdata = valor;
	var strformat = mDateFormat.toUpperCase();
	if(tipo == 1){
		strformat += " HH:MM";
	}else if(tipo == 2){
		strformat = "HH:MM";
	}
	if(trim(valor).length != trim(strformat).length){
		alert('<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>');
		fields.focus();
		return false;
	}
	for(var n=0; n < strformat.length; n++){
		if(strformat.charAt(n) >= 'A' && strformat.charAt(n) <= 'Z' && isNaN(valor.charAt(n))){
			alert('<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>');
			fields.focus();
			return false;
		}
	}
	return true;
}

function addTexto(txtarea, text) {
	text = ' ' + text + ' ';
	if (txtarea.createTextRange && txtarea.caretPos) {
		var caretPos = txtarea.caretPos;
		caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == ' ' ? text + ' ' : text;
		//txtarea.focus();
	} else {
		txtarea.value  += text;
		//txtarea.focus();
	}
        alert(txtarea.value);
}

function RetiraAcentos(Campo) {
   var Acentos =  "·‡„‚‚¡¿√¬ÈÍ… ÌÕÛıÙ”‘’˙¸⁄‹Á«abcdefghijklmnopqrstuvxwyz";
   var Traducao = "AAAAAAAAAEEEEIIOOOOOOUUUUCCABCDEFGHIJKLMNOPQRSTUVXWYZ";
   var Posic, Carac;
   var TempLog = "";
   for (var i=0; i < Campo.length; i++)
   {
   Carac = Campo.charAt (i);
   Posic  = Acentos.indexOf (Carac);
   if (Posic > -1)
	  TempLog += Traducao.charAt (Posic);
   else
      TempLog += Campo.charAt (i);
   }
      return (TempLog);
}


function MaxLengthCorte(obj,tam)
{
	if (obj.value.length*1>tam*1) {
		obj.value = obj.value.substring(0, tam);
                var msg = "<%=( termo ? user.getTermo("VALIDACAMPOTRUNC") : "O conte˙do do campo est· sendo truncado para xxx caracteres." )%>";
                msg = msg.replace('xxx',tam);
                alert(msg);
	}
}

function maxLengthCountChar(pObj, pLength, pTarget){
  if (pObj.value.length > pLength) {
    pObj.value = pObj.value.substring(0, pLength);
    while(pObj.value.length > pLength){
      pLength--;
      pObj.value = pObj.value.substring(0, pLength);
    }
    var lMsg = "<%=( termo ? user.getTermo("VALIDACAMPOTRUNC") : "O conte˙do do campo est· sendo truncado para xxx caracteres." )%>";
    lMsg = lMsg.replace('xxx', pLength);
    alert(lMsg);
  }
  try{document.getElementById(pTarget).innerHTML = '[' + pObj.value.length + ']';}catch(ex){}
}

/*function MaxLength(obj,tam)
{
	if (obj.value.length*1>=tam*1)
		event.keyCode = 0;
}*/

function MaxLength(obj, max)
{
  str = obj.value;
  if( str.length > max )
  {
    obj.value = str.substring( 0, max );
  }
}
//Nova Funcao Mask Integer
function integerMaskWithSignal(obj, length, noSignal){
var isMultipleValues = 0;
var length = length;
var oldValue = obj.value;
var position = getCursorPosition(obj);
var values = fixSemicolon(obj.hasFocus, obj.value).split(';');
var newValue = '';
obj.value = '';
for(i = 0; i < values.length; i++){
  if(newValue != ''){
    newValue += ';';
  }
  var signal = (noSignal?'':(values[i].indexOf('-') == 0?'-':''));
  var value = values[i].replace(/[^0-9]/g, '');
  if(value.length > length){
    value = value.substring(0, length);
  }
  newValue += signal + value;
  if(!isMultipleValues){
    i = values.length;
  }
}
obj.value = fixSemicolon(obj.hasFocus, newValue);
position += obj.value.length - oldValue.length;
setCursorPosition(obj, position);
}


function FormataCalculo(str)
{
	var valor = ""+str
	if (valor+""=="") valor = "0";
	<%if (!user.getUserNumberMil().equals("")) {%>
	if ((valor.indexOf("<%=user.getUserNumberMil()%>")>=0)&&(valor.indexOf("<%=user.getUserNumberDec()%>")>=0)){
		if (valor.indexOf("<%=user.getUserNumberMil()%>") < valor.indexOf("<%=user.getUserNumberDec()%>")) // 1.200,00
		{
			valor = replaceAll(valor, "<%=user.getUserNumberMil()%>","");
		}
		else if (valor.indexOf("<%=user.getUserNumberMil()%>") > valor.indexOf("<%=user.getUserNumberDec()%>")) // 1,200.00
		{
			valor = replaceAll(valor, "<%=user.getUserNumberDec()%>", "")
			valor = replaceAll(valor, "<%=user.getUserNumberMil()%>","<%=user.getUserNumberDec()%>");
		}
	}
        <%}%>
	valor = parseFloat(replaceAll(valor,"<%=user.getUserNumberDec()%>",".")) // 1200,00 , 1200.00
	return valor
}

function TamanhoJanela(w,h)
{
  	self.resizeTo(w,h)
	self.moveTo((screen.availWidth/2)-(w/2),(screen.availHeight/2)-(h/2))
}

function ShowHideLayer(layerNome)
{

  var elemLayerID = document.getElementById(layerNome);

  if (elemLayerID.style.display == 'none')
  {
    elemLayerID.style.display = '';
  }
  else {
    elemLayerID.style.display = 'none';
  }
}

function FormatNumber(num,dec)
{
	return decimalFormat(num,dec);
}


function MascaraData(obj)
{

  	evt = event
  	var Sep = ""
	if (obj.value.length == 10)
		evt.keyCode = 0
	else{
          lPos = mDateFormat.toLowerCase().indexOf("yyyy")
          if (lPos == 0){
            if (obj.value.length==4)
            	obj.value = obj.value+mDateFormat.toLowerCase().charAt(4)
            else if (obj.value.length==7)
            	obj.value = obj.value+mDateFormat.toLowerCase().charAt(7)
          }
          if (lPos == 3){
            if (obj.value.length==2)
            	obj.value = obj.value+mDateFormat.toLowerCase().charAt(2)
            else if (obj.value.length==7)
            	obj.value = obj.value+mDateFormat.toLowerCase().charAt(7)
          }
          if (lPos == 6){
            if (obj.value.length==2)
            	obj.value = obj.value+mDateFormat.toLowerCase().charAt(2)
            else if (obj.value.length==5)
            	obj.value = obj.value+mDateFormat.toLowerCase().charAt(5)
          }
	}

	MascaraInteiro()
}

function MascaraMesAno(obj)
{
	if (obj.value.length == 7)
		event.keyCode = 0
	else
		if (obj.value.length==2)
			obj.value = obj.value+"/"
	MascaraInteiro()
}

function FormataReducao(obj)
{
	if (obj.value.length == 2 || obj.value.length == 3) {
		if (obj.value.indexOf(":") < 0) {
			obj.value = obj.value.substring(0,1) + ":" + obj.value.substring(1,obj.value.length)
		}
	}
}

function MascaraReducao(obj)
{
	if (obj.value.length > 3) {
		event.keyCode = 0
	} else {
		if (obj.value.length == 1)
			obj.value = obj.value+":"
	}
	MascaraInteiro()
}


function MascaraHora(obj,seg)
{
	if (obj.value.length == 8)
		event.keyCode = 0
	else
		if (obj.value.length==2 || obj.value.length==5)
			obj.value = obj.value+":"
	MascaraInteiro()
}


function FormatHora(obj,seg)
{
	if (obj.value.length==0) return
	var valHora = true
	if (obj.value.indexOf(":")>0)
	{
		var hora = obj.value.split(":")
		if (hora.length==2)
		{
			var a = parseFloat(hora[0])
                        var b = 0
                        if (hora[1]+'' != '')
                        	var b = parseFloat(hora[1])
			var c = 0
		}
		else if(hora.length==3)
		{
			var a = parseFloat(hora[0])
			var b = parseFloat(hora[1])
			if (hora[2]+'' == '' )
			{
				valHora = false
				var c = 0
			}
			else
			{
				var c = parseFloat(hora[2])
			}
		}
		if (a>23) valHora = false
		else if (b>59) valHora = false
		else if (c>59) valHora = false
	}
	else
	{
		var a = parseFloat(obj.value)
              	var b = 0
              	var c = 0
                if (a>23) valHora = false
	}
	if (!(valHora))
	{
		alert("<%=termo ? user.getTermo("HORAINVALIDA") : "Hora Inv·lida!"%>")
		obj.value = ""
		obj.focus()
	}
        else
	{
               if (a<10)
               	    obj.value = "0"+a
               else
                    obj.value = a

              if (b<10)
               	    obj.value += ":0"+b
               else
                    obj.value += ":"+b
              if (seg) {
              	if (c<10)
               	    	obj.value += ":0"+c
               	else
                    	obj.value += ":"+c
              }
	}

}

function FormatMesAno(obj)
{
	var m,a,data,valData
	if (obj.value.length==0) return
	valData = true
	data = obj.value.split("/")
	if (data.length == 2)
	{
		m = parseFloat(data[0])
		a = parseFloat(data[1])

                if (m==0)
                {
			alert("<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>")
			obj.value = ""
			obj.focus()
		}

		if (a.length==3) a = "2"+a
		else if (a.length==2) a = "20"+a
		else if (a.length==1) a = "200"+a

		if ((isNaN(m))||(isNaN(a))) valData = false
                if (m>12) valData = false
		if (!(valData))
		{
			alert("<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>")
			obj.value = ""
			obj.focus()
		}
		else
                {
               		if (m.toString().length < 2)
                  		obj.value = "0" + m + "/" + a
                        else
                        	obj.value = m + "/" + a
                }
	}
	else
	{
		alert("<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>")
		obj.value = ""
		obj.focus()
	}
}

function validaMesAno(pData) {
    if(pData.value+'' != ''){
      if(pData.value.length == 7){
        if(pData.value.split('/')[0]*1 > 12){
          alert("<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>")
          pData.value = ""
          pData.focus()
          return false
        }
      }
      else{
      	alert("<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>")
        pData.value = ""
        pData.focus()
        return false
      }
    }
    return true
}

function getDatePt(lStr){
	//Buscando separadores
        Sep1 = ""
        Sep2 = ""
        ano = 0
        mes = 0
        dia = 0
        lPos = mDateFormat.toLowerCase().indexOf("yyyy")
        if (lPos == 0){
          Sep1 = mDateFormat.toLowerCase().charAt(4)
          Sep2 = mDateFormat.toLowerCase().charAt(7)
          ano = 0
          mes = (mDateFormat.toLowerCase().indexOf("mm")>mDateFormat.toLowerCase().indexOf("dd"))?2:1
          dia = (mDateFormat.toLowerCase().indexOf("dd")>mDateFormat.toLowerCase().indexOf("mm"))?2:1
        }
        if (lPos == 3){
          Sep1 = mDateFormat.toLowerCase().charAt(2)
          Sep2 = mDateFormat.toLowerCase().charAt(7)
          ano = 1
          mes = (mDateFormat.toLowerCase().indexOf("mm")>mDateFormat.toLowerCase().indexOf("dd"))?2:0
          dia = (mDateFormat.toLowerCase().indexOf("dd")>mDateFormat.toLowerCase().indexOf("mm"))?2:0
        }
        if (lPos == 6){
          Sep1 = mDateFormat.toLowerCase().charAt(2)
          Sep2 = mDateFormat.toLowerCase().charAt(5)
          ano = 2
          mes = (mDateFormat.toLowerCase().indexOf("mm")>mDateFormat.toLowerCase().indexOf("dd"))?1:0
          dia = (mDateFormat.toLowerCase().indexOf("dd")>mDateFormat.toLowerCase().indexOf("mm"))?1:0
        }

        var dataAux = new Array("","","")
        if (Sep1==Sep2){
          dataAux[0] = lStr.split(Sep1)[0]
          dataAux[1] = lStr.split(Sep1)[1]
          dataAux[2] = lStr.split(Sep1)[2]
        }
        else{
          dataAux[0] = lStr.split(Sep1)[0]
          dataAux[1] = lStr.split(Sep1)[1]
          dataAux[1] = dataAux[1].split(Sep2)[0]
          dataAux[2] = lStr.split(Sep2)[1]
        }
        return dataAux[dia]+"/"+dataAux[mes]+"/"+dataAux[ano]

}

function FormatDate(obj)
{
	var d,m,a,data,valData
	if (obj.value.length==0) return
	valData = true
	//alert("Antes = " + obj.value)
        strAux = getDatePt(obj.value)
        //alert("Depois = " + strAux)
        data = strAux.split("/")

	if (data.length == 3){
		d = parseFloat(data[0])
		m = parseFloat(data[1])
		a = data[2]

                if (d==0||m==0)
                {
			alert("<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>")
			obj.value = ""
			obj.focus()
		}

		if (a.length==3) a = "2"+a
		else if (a.length==2) a = "20"+a
		else if (a.length==1) a = "200"+a

		a = parseFloat(a)
		if (a%4==0)
		{
			if ((m==2)&&(d>29)) valData = false
			else if ((d>30)&&((m==4)||(m==6)||(m==9)||(m==11))) valData = false
			else if (d>31) valData = false
			else if (m>12) valData = false
		}
		else
		{
			if ((m==2)&&(d>28)) valData = false
			else if ((d>30)&&((m==4)||(m==6)||(m==9)||(m==11))) valData = false
			else if (d>31) valData = false
			else if (m>12) valData = false
		}
		if ((isNaN(m))||(isNaN(d))||(isNaN(a))) valData = false
		if (!(valData))
		{
			alert("<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>")
			obj.value = ""
			obj.focus()
		}
		else
			obj.value = exibeData(d,m,a)
	}
	else
	{
		alert("<%=termo ? user.getTermo("DATAINVALIDA") : "Data Inv·lida!"%>")
		obj.value = ""
		obj.focus()
	}
}

function goLink(strUrl,strName,W,H,features)
{
	var L = (screen.width/2) - (W/2)
	var T = (screen.height/2) - (H/2) - 24
	var win = window.open(strUrl,strName,"width="+W+",height="+H+",Top="+T+",Left="+L+","+features)
	return win
}

function trim(str){
  var regExpressionObj = /^\s+|\s+$/g;
  return str.replace(regExpressionObj,"");
}

function MascaraNumeroVirgula()
{
	var k = event.keyCode
	if ((k>57||k<48)&&k!=44&&k!=13)
		event.keyCode = 0;
}

function MascaraReal()
{
	var k = event.keyCode
	if ((k>57||k<48)&&k!=44&&k!=46&&k!=13&&k!=45)
		event.keyCode = 0;
}

function MascaraPorcentagem()
{
	var k = event.keyCode
	if ((k>57||k<48)&&k!=44&&k!=46&&k!=37&&k!=13)
		event.keyCode = 0;
}

function MascaraInteiro(e) {
  if ( !e ) e = window.event;

  var k = e.keyCode || e.which;
  if ((k > 57 || k < 48) && k != 13 && k != 45 && k != 8 && k != 46) {
    e.keyCode = 0;
    e.wich = 0;
    e.preventDefault();
  }
}

function MascaraCEP(obj)
{
  	/* Removido PQ CEP internacional n„o exige formataÁ„o (Victtor)
	var str = ""
        MascaraInteiro();
	for (var i=0;i<obj.value.length;i++)
		if (obj.value.substring(i,i+1)!="." && obj.value.substring(i,i+1)!="-" && obj.value.substring(i,i+1)!="/")
			str += obj.value.substring(i,i+1)
	var tam = str.length
        if (tam>=5) obj.value = str.substring(0,2)+"."+str.substring(2,5)+"-"+str.substring(5,tam)
        else if (tam>=2) obj.value = str.substring(0,2)+"."+str.substring(2,tam)
	else if (tam==10) event.keyCode = 0;
        */
}

function MascaraCPF(obj)
{
	var str = ""
	for (var i=0;i<obj.value.length;i++)
		if (obj.value.substring(i,i+1)!="." && obj.value.substring(i,i+1)!="-" && obj.value.substring(i,i+1)!="/")
			str += obj.value.substring(i,i+1)
	var tam = str.length
	if (tam==3) obj.value = str + "."
	else if (tam==6) obj.value = str.substring(0,3)+"."+str.substring(3,7)+"."
	else if (tam>=9 && tam<11) obj.value = str.substring(0,3)+"."+str.substring(3,6)+"."+str.substring(6,9)+"-"+str.substring(9,str.length)
	else if (tam==11) event.keyCode = 0;
}

function MascaraCPF_CNPJ(obj)
{
	//if (event.keyCode==8) return false
	var str = ""
	for (var i=0;i<obj.value.length;i++)
		if (obj.value.substring(i,i+1)!="." && obj.value.substring(i,i+1)!="-" && obj.value.substring(i,i+1)!="/")
			str += obj.value.substring(i,i+1)
	var tam = str.length
	if (tam==3) obj.value = str + "."
	else if (tam==6) obj.value = str.substring(0,3)+"."+str.substring(3,7)+"."
	else if (tam>=9 && tam<11) obj.value = str.substring(0,3)+"."+str.substring(3,6)+"."+str.substring(6,9)+"-"+str.substring(9,str.length)
	else if (tam==11) obj.value = str.substring(0,2)+"."+str.substring(2,5)+"."+str.substring(5,8)+"/"+str.substring(8,11)
	else if (tam==12) obj.value = str.substring(0,2)+"."+str.substring(2,5)+"."+str.substring(5,8)+"/"+str.substring(8,12)+"-"
	else if (tam==14) event.keyCode = 0;
}

function CompareDate(data1, data2) {
   if (data1 != null && data2 != null && data1 != '' && data2 != '') {
     	data1 = getDatePt(data1)
	lDia1 = data1.split('/')[0];
        lMes1 = data1.split('/')[1];

	if (lDia1.length < 2) {
	   lDia1 = '0' + lDia1;
	}

	if (lMes1.length < 2) {
	   lMes1 = '0' + lMes1;
       }

	lData1 = data1.split('/')[2] + lMes1 + lDia1;

        data2 = getDatePt(data2)
	lDia2 = data2.split('/')[0];
        lMes2 = data2.split('/')[1];

	if (lDia2.length < 2) {
	   lDia2 = '0' + lDia2;
	}

	if (lMes2.length < 2) {
	   lMes2 = '0' + lMes2;
	}

	lData2 = data2.split('/')[2] + lMes2 + lDia2;

        if (lData1 > lData2) {
                return true;
        } else {
                return false;
        }
   }
  return false;
}

function CampoReal(obj,num){
        obj.onkeypress = function(){MascaraInteiro()}
        obj.oncontextmenu = function() {return false}
        obj.onkeyup = function(){FormataValor(obj,event,num)}
}

function validaCheckBox(obj) {
  for (i = 0; i < obj.length; i++) {
     if (obj[i].checked) {
        return true
     }
  }
  return false
 }

function somaData(pDate,pDias){
           var lDate = getDatePt(pDate)
           var d = parseInt(lDate.split("/")[0],10)
           var m = parseInt(lDate.split("/")[1],10)
           var a = parseInt(lDate.split("/")[2],10)
           d = parseInt(pDias,10)+d
           var mes = new Array(31,29,31,30,31,30,31,31,30,31,30,31)
           var bis = (a % 4 == 0)?28:29;
           var correto = false
           var lAux = 0
           while (!correto){
                       if (m==2 && d>bis){
                                  m=3
                                  d=d-bis
                       }
                       else if (d > mes[m-1]){
                                  lAux = mes[m-1]
                                  if (m==12){
                                              m=1
                                              a++
                                  }
                                  else
                                              m++
                                  d=d-lAux
                       }
                       else
                                  correto = true
           }
           return exibeData(d,m,a)
}

function exibeData(d,m,a){
   data = mDateFormat.toLowerCase().replace("dd",d)
   data = data.replace("mm",m)
   data = data.replace("yyyy",a)
   return data
}


/*
var MASK_CEP = '#####-###';
var MASK_CEP = '#####-###';
var MASK_CPF = '###.###.###-##';
var MASK_CNPJ = '##.###.###/####-##';
var MASK_DATA = '##/##/####';
var MASK_DATATIME = '##/##/#### ##:##';
var MASK_TELEFONE = '##.####-####';
*/

function formataMascara(field, mascara) {
	mascara = mascara.split('[|]');
	var severalMasks = (mascara.length>1 ? true : false);

	var i;
	for (i=0; i<mascara.length; i++) {
		if ((field.value.length <= mascara[i].length) || (!severalMasks)) {
			break;
		}
	}


        if(field.value.length >= mascara[i].length && severalMasks && i<mascara[i].length) {
            i++;
        }

        var format = mascara[i];

        maxLength(field, format.length);

	var result = "";
	var maskIdx = format.length - 1;
	var error = false;
	var valor = field.value;
	var posFinal = false;
	if( field.setSelectionRange )
	{
    	if(field.selectionStart == valor.length)
    		posFinal = true;
    }
	valor = valor.replace(/[^0123456789]/g,'')
	for (var valIdx = valor.length - 1; valIdx >= 0 && maskIdx >= 0; --maskIdx)
	{
		var chr = valor.charAt(valIdx);
		var chrMask = format.charAt(maskIdx);
		switch (chrMask)
		{
		case '#':
			if(!(/\d/.test(chr)))
				error = true;
			result = chr + result;
			--valIdx;
			break;
		case '@':
			result = chr + result;
			--valIdx;
			break;
		default:
			result = chrMask + result;
		}
	}

	field.value = result;
	field.style.color = error ? 'red' : '';
	if(posFinal)
	{
		field.selectionStart = result.length;
		field.selectionEnd = result.length;
	}
	return result;
}

function maxLength(obj, max) {
  str = obj.value;
  if( str.length >= max ) {
    event.keyCode = 0;
  }
}

function validateFields() {
  size = arguments.length;
  for(var i=0; i<size; i++) {
    obj = arguments[i];
    if(trim(obj.value) == '') {
      alert("<%=termo ? user.getTermo("CAMPOSOBRIGAT") : "Preencha todos os campos obrigatÛrios!"%>");
      obj.focus();
      return false;
    }
  }
  return true;
}

function validaRadioButton(obj) {
  for (i = 0; i < obj.length -1; i++) {
     if (obj[i].checked) {
        return true
     }
  }
  return false
}

function getRadioSelectedValue(obj) {
  for (i = 0; i < obj.length; i++) {
     if (obj[i].checked) {
        return obj[i].value;
     }
  }
  return '';
}

function Limpar(valor, validos) {
// retira caracteres invalidos da string
var result = "";
var aux;
for (var i=0; i < valor.length; i++) {
aux = validos.indexOf(valor.substring(i, i+1));
if (aux>=0) {
result += aux;
}
}
return result;
}

function decimalFormat(pValue, pCasas) {
    var lSepMil = "<%=user.getUserNumberMil()%>"
    var lSepDec = "<%=user.getUserNumberDec()%>"
    var lZeros = "";
    var lReturn = "";

    //Criando String de Zeros...
    for (i=0;i<20;i++) lZeros += "0";

    //Verificando valor nulo...
    if (pValue+"" == ""){
      pValue = "0";
    }

    var lDec = "";
    var lMil = "";


    //Quebrando partes
    pValue = ""+pValue;
    if (pValue.indexOf(".")>=0){
      lDec = pValue.split(".")[1];
      lMil = pValue.split(".")[0];
    }
    else{
      lDec = "";
      lMil = pValue;
    }

    //Definindo tamanho da parte decimal
    if (pCasas>0){
      lDec = (lDec+lZeros).substring(0,pCasas);
    }
    else{
      lDec = "";
    }

    //Preparando o milhar
    if (lSepMil != ""){
      lNovo = "";
      lInt = lMil.length;
      while (lInt > 3) {
        lInt = lInt - 3;
        lNovo = lMil.substring(lInt, lInt+3) + (lNovo.length > 0 ? lSepMil : "") + lNovo;

      }
      lMil = lMil.substring(0, lInt) + (lNovo.length > 0 ? lSepMil : "") + lNovo;
    }

    if (lDec == ""){
      lReturn = lMil;
    }
    else{
      lReturn = lMil + lSepDec + lDec;
    }


    return lReturn;
  }

  function fixSemicolon(hasFocus, value){
    while(value.indexOf(';;') > -1){
      value = value.replace(';;', ';');
    }
    while(value.indexOf('-;') > -1){
      value = value.replace('-;', '');
    }
    if(value.indexOf(';') == 0){
      value = value.substring(1);
    }
    if(!hasFocus){
      var lastPosition = value.length - 1;
      var lastChar = value.charAt(lastPosition);
      if(lastChar == '-' || lastChar == ';'){
        value = value.substring(0, lastPosition);
      }
    }
    return value;
  }
  function getCursorPosition(obj) {
    var position = 0;
    if (typeof(obj.selectionStart) != 'undefined') {
      position = obj.selectionStart;
    }else if (document.selection) {
      var range = document.selection.createRange();
      var textRange = obj.createTextRange();
      var storedRange = textRange.duplicate();
      textRange.moveToBookmark(range.getBookmark());
      storedRange.setEndPoint('EndToEnd', textRange);
      position = storedRange.text.length;
    }
    return position;
  }
  function setCursorPosition(obj, position) {
    if(obj.hasFocus){
      if(position > obj.value.length){
        position = obj.value.length;
      }
      if (obj.setSelectionRange) {
        obj.focus();
        obj.setSelectionRange(position, position);
      }else if (obj.createTextRange) {
        var breaks = obj.value.slice(0, position).match(/\\n/g);
        var endPoint = 0;
        if (breaks instanceof Array) {
          endPoint = -breaks.length;
        }
        var range = obj.createTextRange();
        range.collapse(true);
        range.moveStart('character', position);
        range.moveEnd('character', endPoint);
        range.select();
      }
    }
  }
  function removeEspacos(str){
    var oldValue = str.value;
    var position = getCursorPosition(str);
    var regExpressionObj = /\s/gi;
    str.value = str.value.replace(regExpressionObj,"");
    position += str.value.length - oldValue.length;
    setCursorPosition(str, position);
    alert("<%=user.getTermo("MSGNAOESPABRANC")%>"+"\n"+"<%=user.getTermo("MSGESPABRANCREM")%>");
  }

  //decimalMask
  function FormataValor(obj,teclapres,casas,maxLength){
    var isMultipleValues = 0;
    var intLength = maxLength;
    var decLength = casas;
    var position = getCursorPosition(obj);
    var values = fixSemicolon(obj.hasFocus, obj.value).split(';');
    var oldValue = obj.value;
    var newValue = '';
    obj.value = '';
    for(i = 0; i < values.length; i++){
      if(newValue != ''){
        newValue += ';';
      }
      var intPart = '';
      var decPart = '';
      var signal = (values[i].indexOf('-') == 0?'':'');
      var value = values[i].replace(/[^0-9]/g, '');
      newValue += signal;
      if(value.length > 0){
        if(value > intLength + decLength){
          value = value.substring(0, intLength + decLength);
        }
        if(value.length > decLength){
          decPart = value.substring(value.length - decLength);
          value = value.substring(0, value.length - decLength).replace(/^0+/, '');
          var j = 0;
          for(var k = value.length - 1; k >= 0; k--){
            if(j == 3 && casas != 0){
              intPart = '<%=user.getUserNumberMil()%>' + intPart;
              j = 0;
            }
            intPart = value.charAt(k) + intPart;
            j++;
          }
          if(intPart == ''){
            intPart = '0';
          }
        }else{
          intPart = '0';
          decPart = value;
          for(var k = 0; k < decLength - value.length; k++){
            decPart = '0' + decPart;
          }
        }
        if(decPart == ''){
        	newValue += intPart;
        }else{
        	newValue += intPart + '<%=user.getUserNumberDec()%>' + decPart;
        }
      }
      if(!isMultipleValues){
        i = values.length;
      }
    }
    obj.value = fixSemicolon(obj.hasFocus, newValue);
    position += obj.value.length - oldValue.length;
    setCursorPosition(obj, position);
  }


/*function FormataValor(obj,teclapres,casas,maxLength) {

	var lSepMil = "<%=user.getUserNumberMil()%>"
	var lSepDec = "<%=user.getUserNumberDec()%>"
	var tecla = teclapres.keyCode;
	var lNovo = ""
	var vr = "";
	var validos = "0123456789"
	var valor = obj.value;
	var aux;
	var lDec = ""

	for (var i=0; i < valor.length; i++) {
		aux = validos.indexOf(valor.substring(i, i+1));
		if (aux>=0) {
			vr += aux;
		}
	}

        tam = vr.length;

        if (maxLength){
    	    if (tam>maxLength){
                teclapres.returnValue = false
                teclapres.keyCode = 0;
                vr = vr.substr(0,maxLength)
                tam = vr.length;
    	    }
      	}



	if (casas>0){
		lDec = vr.substr(tam-casas)
	}

	var lMil = vr.substr(0,tam-casas)





	if (lSepMil!=""){
		tam = lMil.length;
		lInt = tam
		while (lInt > 3){
			lInt = lInt-3
			lNovo = lMil.substr(lInt,3) + (lNovo.length>0?lSepMil:"") + lNovo
		}
		lNovo = lMil.substr(0,lInt) + (lNovo.length>0?lSepMil:"") + lNovo
	}
        else{
           lNovo = lMil
        }


	if (lNovo=="" & lDec!=""){
		lNovo = "0"
	}
	else if (lNovo.length>=2 && lNovo.substr(0,1)=="0"){
		lNovo = lNovo.substring(1)
	}

	obj.value = lNovo + (lNovo.length>0?lSepDec:"") + lDec
}*/

function replaceAll(pText, pSearch, pReplace){
   pText = ""+pText;
   if (pSearch!=pReplace){
       while (pText.indexOf(pSearch)>=0){
           pText = pText.replace(pSearch,pReplace);
       }
   }
   return pText;
}

function showFlash(pArquivo, pLargura, pAltura, pBgcolor, pId, pQualidade, pAlinhamento, pTransparente, pAddParams) {
	obj = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="'+pLargura+'" height="'+pAltura+'" id="'+pId+'" align="'+pAlinhamento+'">';
	obj+= '<param name="movie" value="'+pArquivo+'" />';
	obj+= '<param name="quality" value="'+pQualidade+'" />';
	if(pTransparente) {
		obj+= '<param name="wmode" value="transparent" />';
	}
        if (pAddParams!="") {
           obj+= pAddParams;
        }
	obj+= '<param name="bgcolor" value="'+pBgcolor+'" />';
	obj+= '<embed src="'+pArquivo+'" '+((pTransparente)?'wmode="transparent"':'')+'" quality="'+pQualidade+'" bgcolor="'+pBgcolor+'" width="'+pLargura+'" height="'+pAltura+'" name="'+pId+'" align="'+pAlinhamento+'" allowScriptAccess="sameDomain"  swliveconnect="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer"  />';
	obj+= '</object>';
	document.write(obj);
//        return obj;
}


function makeRequest(tipo,url,post,returnFunction) {
	getUrl(url,true,execOnSuccess(returnFunction,tipo))
}

function makeRequestSync(tipo,url,post,returnFunction) {
	getUrl(url,false,execOnSuccess(returnFunction,tipo))
}


/*function $() {
	var lElements = new Array();
	for (var i = 0; i < arguments.length; i++) {
		var lElement = arguments[i];
		if (typeof lElement == 'string') {
			lElement = document.getElementById(lElement);
		}
	    if (arguments.length == 1) {
			return lElement;
		}
		lElements.push(lElement);
	}
	return lElements;
}*/

function doLogin() {
	if (trim(document.frm_login.login.value) == '' || trim(document.frm_login.senha.value) == '') {
                alert("<%=termo ? user.getTermo("MSGSENHAINVALIDA") : "Senha Inv·lida!"%>");
                /*alert('Favor preencher os campos Usu·rio e Senha.');*/
	}
	else {
		document.frm_login.submit();
	}
}

function ShowHideLayer(pLayer)
{
  var lLayer = document.getElementById(pLayer);
  if (lLayer.style.display == 'none') { lLayer.style.display = ''; }
  else { lLayer.style.display = 'none'; }
}

function validateFields() {
	size = arguments.length;
	for(var i=0; i<size; i++) {
		obj = arguments[i];
		if(obj && trim(obj.value) == '') {
			alert("<%=termo ? user.getTermo("CAMPOSOBRIGAT") : "Preencha todos os campos obrigatÛrios!"%>");
			try{ obj.focus(); } catch(e) {}
			return false;
		}
	}
	return true;
}


function MascaraCPF_CNPJ(obj)
{
	var str = ""
	for (var i=0;i<obj.value.length;i++)
		if (obj.value.substring(i,i+1)!="." && obj.value.substring(i,i+1)!="-" && obj.value.substring(i,i+1)!="/")
			str += obj.value.substring(i,i+1)
	var tam = str.length
	if (tam==3) obj.value = str + "."
	else if (tam==6) obj.value = str.substring(0,3)+"."+str.substring(3,7)+"."
	else if (tam>=9 && tam<11) obj.value = str.substring(0,3)+"."+str.substring(3,6)+"."+str.substring(6,9)+"-"+str.substring(9,str.length)
	else if (tam==11) obj.value = str.substring(0,2)+"."+str.substring(2,5)+"."+str.substring(5,8)+"/"+str.substring(8,11)
	else if (tam==12) obj.value = str.substring(0,2)+"."+str.substring(2,5)+"."+str.substring(5,8)+"/"+str.substring(8,12)+"-"
	else if (tam==14) event.keyCode = 0;
}


// AJAX
function getXmlHttpRequest() {
	var httpRequest = null;
	try {
		httpRequest = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
		try {
			httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e) {
			httpRequest = null;
		}
	}
	if (!httpRequest && typeof XMLHttpRequest != "undefined") {
		httpRequest = new XMLHttpRequest();
	}
	return httpRequest;
}

function getUrl(url, async, handleStateChange) {

      	var xmlHttpReq = getXmlHttpRequest();
	if (!xmlHttpReq) return;
      	only_url = (url.indexOf('?')>0)?url.substring(0,url.indexOf('?')):url;
      	data = (url.indexOf('?')>0)?url.substring(url.indexOf('?')+1):null;

      	postUrl(only_url, data, async, handleStateChange);
}
function execOnSuccess(stateChangeCallback) {
	//Criando variaveis de argumentos dinamica..
	var a = arguments;
	for (i=1; i < a.length; i++) {
		eval("var lArg"+i+" = a[i];");
	}
	return function(xmlHttpReq) {
			if (xmlHttpReq.readyState == 4 && xmlHttpReq.status == 200) {
                            if (xmlHttpReq.responseText.indexOf('func'+'tion openL'+'ogin()') > 0) {
                               var lStr = xmlHttpReq.responseText;
		                       var lEval = '';
		                       if (lStr.indexOf('<scr'+'ipt') >= 0) {
		                         var lScript = lStr.split('<scr'+'ipt');
		                         for (i = 0; i < lScript.length; i++) {
		                           if (lScript[i].indexOf('</scr'+'ipt>') != '-1') {
		                             lScriptAux = lScript[i].split('</scr'+'ipt>');
		                             lEval += lScriptAux[0].substring(lScriptAux[0].indexOf('>') + 1);
		                           }
		                         }
		                       }
		                       if (lEval != '') {
		                         lEval = lEval.replace('function submitForm()', 'function oldSubmitForm()');
		                         lEval += "function submitForm() { void(0); }";
		                         
		                         jQuery.globalEval(lEval);
		                       }
                            }
                            else{
				if (a.length > 1) { // se tiver argumentos
					lArgs = "";
					for (i=1; i < a.length; i++) {
						if (lArgs != "") { lArgs += ", "; }
						lArgs = "lArg"+i;
					}
					eval("stateChangeCallback(xmlHttpReq, "+lArgs+");");
				} else {
					stateChangeCallback(xmlHttpReq);
				}
                            }
			}
		};
}

//Postar formul·rio com ajax..
function getFormParameters(form) {
	var formVars = new Array();
	for (var i = 0; i < form.elements.length; i++)
	{
		var formElement = form.elements[i];

		// Special handling for checkboxes (we need an array of selected checkboxes..)!
		if(formElement.type=='checkbox' && !formElement.checked) {
			continue;
		}
		var v=new Object;
		v.name=formElement.name;
		v.value=formElement.value;
		formVars.push(v);
	}
      	return urlEncodeDict(formVars)
}

function postUrl(url, data, async, stateChangeCallback) {
	var xmlHttpReq = getXmlHttpRequest();
	if (!xmlHttpReq) return;
	xmlHttpReq.open("POST", url, async);
	xmlHttpReq.onreadystatechange = function() { stateChangeCallback(xmlHttpReq); };
	xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=<%=user.getUserCharset()%>');
	xmlHttpReq.setRequestHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	xmlHttpReq.setRequestHeader("Cache-Control", "post-check=0, pre-check=0");
	xmlHttpReq.setRequestHeader("Cache-Control", "no-cache");
	xmlHttpReq.setRequestHeader("Pragma", "no-cache");
	xmlHttpReq.send(data);
	//alert ('url: ' + url + '\ndata: ' + data);
}
function urlEncodeDict(d) {
	var r = "";
        for (var i=0; i<d.length; i++) { r += "&" + encodeURIComponent(d[i].name) + "=" + encodeURIComponent(d[i].value); } return r;
        //for (var i=0; i<d.length; i++) { r += "&" + encodeURIAjax(d[i].name) + "=" + encodeURIAjax(d[i].value); } return r;
        //for (var i=0; i<d.length; i++) { r += "&" + d[i].name + "=" + d[i].value; } return r;
}

function encodeURIAjax(str){
	str = replaceAll(str,'=',encodeURIComponent('='))
      	str = replaceAll(str,'&',encodeURIComponent('&'))
      	str = replaceAll(str,'<',encodeURIComponent('<'))
      	str = replaceAll(str,'>',encodeURIComponent('>'))
      	return str;
}

function postFormUseIFrame(form, async, successCallback) {
	var ifrmPost = document.createElement('iframe');
	ifrmPost.name = form.name + '_ifrmPost';
	ifrmPost.id = form.name + '_ifrmPost';
        document.appendChild(ifrmPost);
	var fTarget = form.target;
	form.target = ifrmPost.id;
	form.submit();
	form.target = fTarget;
}


//Postar formul·rio com ajax..
function postForm(form, async, successCallback) {
	var formVars = new Array();
	for (var i = 0; i < form.elements.length; i++)
	{
		var formElement = form.elements[i];

		// Special handling for checkboxes (we need an array of selected checkboxes..)!
		if(formElement.type=='checkbox' && !formElement.checked) {
			continue;
		}
		var v=new Object;
		v.name=formElement.name;
		v.value=formElement.value;
		formVars.push(v);
	}
	postUrl(form.action, urlEncodeDict(formVars), async, execOnSuccess(successCallback));
}


function EliminaRepeticao(obj){
	if(obj.value10==1){
 	      	event.keyCode=0
      	        obj.onkeyup = function(){obj.value10=0}
      	}
      	else{
                obj.value10=1
      	}
}

/*NOVA FUN«√O QUE FUNCIONA EM TODOS OS NAVEGADORES*/
function mask(isNum, event, field, mask, maxLength) {

	var keyCode;
	if (event.srcElement)
		keyCode = event.keyCode;
	else if (event.target)
		keyCode = event.which;

	var maskStack = new Array();

	var isDynMask = false;
	if (mask.indexOf('[') != -1)
		isDynMask = true;

	var length = mask.length;

	for (var i = 0; i < length; i++)
		maskStack.push(mask.charAt(i));

	var value = field.value;
	var i = value.length;

	if (keyCode == 0 || keyCode == 8)
		return true;

	if (isNum && (keyCode < 48 || keyCode > 57) && keyCode != 8 && keyCode != 13)
		return false;

	if (!isDynMask && i < length) {

		if (maskStack.toString().indexOf(String.fromCharCode(keyCode)) != -1 && keyCode != 8) {
			return false;
		} else {
			if (keyCode != 8) {
				if (maskStack[i] != '#') {
					var old = field.value;
					field.value = old + maskStack[i];
				}
			}

			if (autoTab(field, keyCode, length)) {
				if (!document.layers) {
					return true;
				} else if (keyCode != 8) {
					field.value += String.fromCharCode(keyCode);
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}

	} else if (isDynMask) {

		var maskChars = "";
		for (var j = 0; j < maskStack.length; j++)
			if (maskStack[j] != '#' && maskStack[j] != '[' && maskStack[j] != ']')
				maskChars += maskStack[j];

		var tempValue = "";
		for (var j = 0; j < value.length; j++) {
			if (maskChars.indexOf(value.charAt(j)) == -1)
				tempValue += value.charAt(j);
		}

		value = tempValue + String.fromCharCode(keyCode);

		if (maskChars.indexOf(String.fromCharCode(keyCode)) != -1) {
			return false;
		} else {

			var staticMask = mask.substring(mask.indexOf(']') + 1);
			var dynMask = mask.substring(mask.indexOf('[') + 1, mask.indexOf(']'));

			var realMask = new Array;

			if (mask.indexOf('[') == 0) {
				var countStaticMask = staticMask.length - 1;
				var countDynMask = dynMask.length - 1;
				for (var j = value.length - 1; j >= 0; j--) {
					if (countStaticMask >= 0) {
						realMask.push(staticMask.charAt(countStaticMask));
						countStaticMask--;
					}
					if (countStaticMask < 0) {
						if (countDynMask >= 0) {
							if (dynMask.charAt(countDynMask) != '#') {
								realMask.push(dynMask.charAt(countDynMask));
								countDynMask--;
							}
						}
						if (countDynMask == -1) {
							countDynMask = dynMask.length - 1;
						}
						realMask.push(dynMask.charAt(countDynMask));
						countDynMask--;
					}
				}
			}

			var result = "";

			var countValue = 0;
			while (realMask.length > 0) {
				var c = realMask.pop();
				if (c == '#') {
					result += value.charAt(countValue);
					countValue++;
				} else {
					result += c;
				}
			}

			field.value = result;

			if (maxLength != undefined &&  value.length == maxLength) {

				var form = field.form;
				for (var i = 0; i < form.elements.length; i++) {
					if (form.elements[i] == field) {
						field.blur();
						//if alterado para quando a m·scara for utilizada no ˙ltimo campo, n„o dÍ mensagem de erro quando tentar colocar o foco no "Salvar"
						//if (form.elements[i + 1] != null)
						if ((form.elements[i + 1] != null) && (form.elements[i + 1].name != "METHOD"))
							form.elements[i + 1].focus();
						break;
					}
				}
			}

			return false;
		}
	} else {
		return false;
	}


	function autoTab(field, keyCode, length) {
		var i = field.value.length;

		if (i == length - 1) {

			field.value += String.fromCharCode(keyCode);

			var form = field.form;
			for (var i = 0; i < form.elements.length; i++) {
				if (form.elements[i] == field) {
					field.blur();
					//if alterado para quando a m·scara for utilizada no ˙ltimo campo, n„o dÍ mensagem de erro quando tentar colocar o foco no "Salvar"
					//if (form.elements[i + 1] != null)
					if ((form.elements[i + 1] != null) && (form.elements[i + 1].name != "METHOD"))
						form.elements[i + 1].focus();
					break;
				}
			}

			return false;
		} else {
			return true;
		}
	}
}

/*NOVA FUN«√O QUE FUNCIONA EM TODOS OS NAVEGADORES*/
function integerMask(event){
	var keyCode;
	if (event.srcElement){
		keyCode = event.keyCode;
	}else if (event.target){
		keyCode = event.which;
	}
        if ((keyCode < 48 || keyCode > 57) && keyCode != 13 && keyCode != 8){
          return false;
        }
}

function getValueFromTag(pString,pTag){
  if(pTag.indexOf('<') < 0){
    pTag = '<' + pTag;
  }
  if(pTag.indexOf('>') < 0){
    pTag += '>';
  }
  if(pString.indexOf(pTag) > -1){
    return pString.substring(pString.indexOf(pTag) + pTag.length, pString.indexOf(pTag.replace('<', '</')));
  }else{
    return '';
  }
}

function validateEmailWithMessege(pField) {
  if (!validateEmail(pField.value)) {
    alert('<%=( termo ? user.getTermo("MSGEMAILINVALIDO") : "O e-mail informado n„o È v·lido" )%>.');
    pField.value = '';
  }
}

function validateEmail(pEmail) {
  var lEmailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
  return lEmailPattern.test(pEmail);
}

function makeUpperCase(pId) {
  document.getElementById(pId).value = document.getElementById(pId).value.toUpperCase();
}

function formAddResp(pndsl, comep) {
  goLink('addcomunicacao?CD_PNDSL='+pndsl+'&CD_COMEP='+comep, 'comunicacao', '610', '300', 'scrollbars=yes');
}

function mouseOverTab(tab,i){
	document.getElementById('tdAba_l_t_'+i).className = 'OVER_LT';
	document.getElementById('tdAba_m_t_'+i).className = 'OVER_MT';
	document.getElementById('tdAba_r_t_'+i).className = 'OVER_RT';

	document.getElementById('tdAba_l_m_'+i).className = 'OVER_LM';
	document.getElementById('tdAba_m_m_'+i).className = 'OVER_MM';
	document.getElementById('tdAba_r_m_'+i).className = 'OVER_RM';
}

function mouseOutTab(tab,i){
	document.getElementById('tdAba_l_t_'+i).className = 'nf_ILT';
	document.getElementById('tdAba_m_t_'+i).className = 'nf_IMT';
	document.getElementById('tdAba_r_t_'+i).className = 'nf_IRT';

	document.getElementById('tdAba_l_m_'+i).className = 'nf_ILM';
	document.getElementById('tdAba_m_m_'+i).className = 'nf_IMM';
	document.getElementById('tdAba_r_m_'+i).className = 'nf_IRM';
}

function getValueByTag(txt,tag){
  var lAux = "";
  var lPosIni = txt.indexOf("<"+tag+">");
  lPosIni += ("<"+tag+">").length;
  var lPosFim = txt.indexOf("</"+tag+">",lPosIni);
  lAux = txt.substring(lPosIni,lPosFim);
  return lAux;
}


function MascaraTEL(obj)
{

	var str = ""
        MascaraInteiro();
	for (var i=0;i<obj.value.length;i++)
		if (obj.value.substring(i,i+1)!="." && obj.value.substring(i,i+1)!="-" && obj.value.substring(i,i+1)!="/")
			str += obj.value.substring(i,i+1)
	var tam = str.length
	if (tam==2) obj.value = "(" + str + ")"
	else if (tam==8) obj.value = str.substring(0,9)+"-"
	//else if (tam>=5 && tam<10) obj.value = str.substring(0,2)+"."+str.substring(2,5)+"-"+str.substring(5,str.length)

}

var idIntervalsLockBackground = new Array();

function closeLockBackground(pId){

    while(idIntervalsLockBackground.length > 0){
      window.clearInterval(idIntervalsLockBackground.shift());
    }
    if(document.getElementById(pId)){
      var lbg = document.getElementById(pId);
      lbg.parentNode.removeChild(lbg);
      if(lbg.contentDiv)
       lbg.contentDiv.style.display = 'none';
    }
  }

  function resizeLockBackground(pId){
    var lbg = document.getElementById(pId);
    lbg.style.height = document.body.scrollHeight + 'px';
    lbg.style.width = document.body.scrollWidth + 'px';
    if(lbg.contentDiv && lbg.contentDiv.isScroll){
      var lContent = lbg.contentDiv;
      lContent.style.top = (document.body.scrollTop + lContent.distY)+ 'px';
    }
  }

  //pID = ID do do LockBackground;
  //pZIndex = ZIndex do LockBackground;
  //pContente (opcional) = referencia da Div absolute sera centralizada na tela.

  function showLockBackground(pId,pZIndex,pContent,pIsScroll,pIsAutoResize){
   var scrollTop =  document.body.scrollTop;
    pIsAutoResize = (typeof pIsAutoResize !== 'undefined' || pIsAutoResize == null) ? pIsAutoResize : true;
    var lbg = document.createElement('DIV');
 lbg.id = pId;
    lbg.style.position = 'absolute';
    lbg.style.display = 'block';
 lbg.style.zIndex = pZIndex;
 lbg.style.top = 0;
 lbg.style.left = 0;
 //try{lbg.style.filter = 'alpha(opacity=65)';}catch(ex){}
 //try{lbg.style.MozOpacity = '65';}catch(ex){}
 //try{lbg.style.opacity = '0.65';}catch(ex){}
 try{lbg.className = 'class_lock_background';}catch(ex){}
 lbg.style.backgroundColor='#ffffff';
 lbg.style.height = document.body.scrollHeight + 'px';
 lbg.style.width = document.body.scrollWidth + 'px';

 if(pContent){
          pContent.style.display = '';
          pContent.distY = ((document.body.clientHeight - pContent.offsetHeight)/2);
      
          pContent.style.left = ((document.body.clientWidth - pContent.offsetWidth)/2) + 'px';
          
          
          if (navigator.appName == 'Netscape'){
          	pContent.style.top = (document.body.scrollTop + pContent.distY)+ 'px';
          }else{
          	var indexIni = navigator.appVersion.indexOf('MSIE');
          	var indexFim = navigator.appVersion.indexOf(';', indexIni); 
          	var versao = parseFloat(navigator.appVersion.substring(indexIni+4, indexFim));
          
          	if(versao > 7){
          		pContent.style.top = (pContent.distY) + 'px';
          	}else{
          		pContent.style.top = (document.body.scrollTop + pContent.distY)+ 'px';
          	}
          }
          pContent.isScroll = (pIsScroll == 'undefined' || pIsScroll == null) ? true : pIsScroll;
          lbg.contentDiv = pContent;
 }
   /*lbg.onkeypress = function(e){
      var e = e || event;
      var k = e.keyCode || e.which;
   if(k==27){closeLockBackground(pId);}
 }
 lbg.focus();*/
        lbg.lockBackground = true;
 document.body.appendChild(lbg);
 		if (pIsAutoResize) {
	        idIntervalsLockBackground.push(window.setInterval("resizeLockBackground('"+pId+"')",500));
	        lbg.setResize = function(pdoResize){
	          if(pdoResize) return false;
	          var pos = 0;
	          while(idIntervalsLockBackground.length > 0){
	            window.clearInterval(idIntervalsLockBackground.shift());
	          }
	        }
	    }
	    
        return lbg;
  }

  function setSessionAttribute(pAttr, pValue) {
  	lUrl = 'cajax?acao=setSessionAttribute&attr='+pAttr+'&value='+pValue;
      	makeRequest('', lUrl, null, _returnAjaxSessionAttr);
  }
  function _returnAjaxSessionAttr(retorno, pUrl) {
    if (retorno.responseText != '') {
      alert(retorno.responseText);
    }
  }

function validaLogin(obj){
      if(obj.value.indexOf(' ') > -1){
      removeEspacos(obj)
}
}
function removeEspacos(str){
    var oldValue = str.value;
    var position = getCursorPosition(str);
    var regExpressionObj = /\s/gi;
    str.value = str.value.replace(regExpressionObj,"");
    position += str.value.length - oldValue.length;
    //setCursorPosition(str, position);
    alert("<%=user.getTermo("MSGNAOESPABRANC")%>"+"\n"+"<%=user.getTermo("MSGESPABRANCREM")%>");
    var objId = str.id;
    if(objId == ''){
      objId = str.name;
    }
    setTimeout("document.getElementById('" + objId + "').select()", 100);
  }

function TableConfig (pIdTable){
  this.tableRef = document.getElementById(pIdTable);
  this.addRow = function (pId, pClass, pOnClick, pIndex){
    	var row = this.tableRef.insertRow(pIndex?pIndex:-1);
        row.id = pId?pId:null;
        row.className = pClass?pClass:null;
        row.onclick = pOnClick?pOnClick:null;
        return row;
  }
  this.addCell = function (pRowRef, pHTML, pId, pClass, pWidth, pHeight, pAlign, pVAlign, pColSpan, pRowSpan, pOnClick, pIndex){
    var row = pRowRef;
    var col = row.insertCell(pIndex?pIndex:-1);
    col.id = pId?pId:null;
    col.className = pClass?pClass:null;
    col.innerHTML = pHTML?pHTML:null;
    col.colSpan = pColSpan?pColSpan:1;
    var w = new String(pWidth);
    col.style.width = pWidth?pWidth == '*'?'100%':pWidth:null;
    col.height = pHeight?pHeight:null;
    col.rowSpan = pRowSpan?pRowSpan:1;
    col.align = pAlign?pAlign:'';
    col.vAlign = pVAlign?pVAlign:'';
    col.onclick = pOnClick?pOnClick:null;
    return col;
  }
  this.removeRow = function(pId,pFromGroup){
    var row = document.getElementById(pId);
    this.tableRef.deleteRow(pFromGroup?row.sectionRowIndex:row.rowIndex);
  }
}


var getWindowInfo = {
    'getViewportY' : function() { // viewport
        var height = 0;
        if( typeof( window.innerWidth ) == 'number' ) {
            height = window.innerHeight;
        }
        else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
            height = document.documentElement.clientHeight;
        }
        else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
            height = document.body.clientHeight;
        }
        return height;
    },
    'getViewportX' : function() {
        var width = 0;
        if( typeof( window.innerWidth ) == 'number' ) {
            width = window.innerWidth;
        }
        else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
            width = document.documentElement.clientWidth;
        }
        else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
            width = document.body.clientWidth;
        }
        return width;
    },
    'getOffsetY' : function() { // scroll top edge offset
        var scrOfY = 0;
        if( typeof( window.pageYOffset ) == 'number' ) {
            scrOfY = window.pageYOffset;
        }
        else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
            scrOfY = document.body.scrollTop;
        }
        else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
            scrOfY = document.documentElement.scrollTop;
        }
        return scrOfY;
    },
    'getOffsetX' : function() {
        var scrOfX = 0;
        if( typeof( window.pageXOffset ) == 'number' ) {
            scrOfX = window.pageXOffset;
        }
        else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
            scrOfX = document.body.scrollLeft;
        }
        else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
            scrOfX = document.documentElement.scrollLeft;
        }
        return scrOfX;
    },
    'getPageX' : function() { // total length
        return $$("body")[0].getWidth();
    },
    'getPageY' : function() {
        return $$("body")[0].getHeight();
    }
}

function getIframeObject(id){
    if (navigator.appName == 'Netscape')
        return document.getElementById(id).contentWindow
    else
    	return eval(id)
}

function innerScript(pScript){
    try{
        execScript(pScript);
    } catch(expt) {
        setTimeout(pScript, 0);
    }
}

function GetWindowWidth() {
	var x = 0;
	if (self.innerHeight) {
	        x = self.innerWidth;
	}
	else if (document.documentElement && document.documentElement.clientHeight) {
	        x = document.documentElement.clientWidth;
	}
	else if (document.body) {
	        x = document.body.clientWidth;
	}
	return x;
  }
function GetWindowHeight() {
	var y = 0;
	if (self.innerHeight) {
	        y = self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientHeight) {
	        y = document.documentElement.clientHeight;
	}
	else if (document.body) {
	        y = document.body.clientHeight;
	}
	return y;
}

function innerScript(pEval) {
  if (window.execScript) {
    window.execScript(pEval);
  }
  else {
    setTimeout(pEval, 0);
  }
}

// REMOVER ACENTO
function removeAcento(palavra) { 
  var com_acento = '·‡„‚‰ÈËÍÎÌÏÓÔÛÚıÙˆ˙˘˚¸Á¡¿√¬ƒ…» ÀÕÃŒœ”“’÷‘⁄Ÿ€‹«Ò—¥`®~^?()&!@#$%=[]\/{}|:<>,."*ß∫∞™+π≤≥£¢¨;:\\';
  var sem_acento = 'aaaaaeeeeiiiiooooouuuucAAAAAEEEEIIIIOOOOOUUUUCnN';  
  var nova='';  
  for(i=0;i<palavra.length;i++) {
    if (com_acento.indexOf(palavra.substr(i,1))>=0) {
      nova+=sem_acento.substr(com_acento.indexOf(palavra.substr(i,1)),1);
    }else{
      nova+=palavra.substr(i,1); 
    }
  }
  return nova;  
}

function doAjax(x) {
  if (x.responseText) {
  	processResponse('', x.responseText);
  }
  else if (typeof x === 'string') {
  	processResponse('', x);
  }
}

function processResponse(id, response) {
  var html = '';
  var js = '';
  if (response.indexOf('<scr' + 'ipt') >= 0) { 
    var jsAux = response.split('<scr' + 'ipt'); 
    for (var i = 0; i < jsAux.length; i++) {
      if (jsAux[i].indexOf('</scr' + 'ipt>') != '-1') {
        scriptAux = jsAux[i].split('</scr' + 'ipt>');
        js += scriptAux[0].substring(scriptAux[0].indexOf('>') + 1);
        html += scriptAux[1];
      }
      else {
        html += jsAux[i];
      }
    }
  }
  else {
    html = response;
  }
  if (id != null && id != '' && html != null) {
  	jQuery('#' + id).html( html );
  }
  if (js!= null && js != '') {
  	if (jQuery.globalEval) {
  		jQuery.globalEval( js )
  	}
  	else {
    	innerScript(js);
    }
  }
}

function resizeTextArea(textarea) {
  textarea = ( textarea.jquery ? textarea : $(textarea) );
  textarea.scrollTop();
  if (textarea[0].clientHeight < textarea[0].scrollHeight) {
    textarea.attr('rows', parseInt(textarea.attr('rows')) + 1);
  }
}

function hideSidebarLayer() {
  $('.page-sidebar-menu > li').removeClass('start active');
  try {
	$( $sidebarItem ).closest('li').addClass('start active');
  }
  catch(e) {}
  
  var contentOverlay = $('#_div_isg_page_content_overlay');
  contentOverlay.offset({ top: 0, left: 0 });
  contentOverlay.css( 'position', 'static' );
  contentOverlay.css( 'z-index', '0' );
  contentOverlay.html( '' );
  contentOverlay.hide();
}

function showSidebarLayer(iframeURL, isFullWidth) {
  Metronic.scrollTo();
  
  $sidebarItem = $('.page-sidebar-menu > li.active');
  
  $('.page-sidebar-menu > li').removeClass('start active');
  try {
	$(event.target).closest('li').addClass('start active');
  }
  catch(e) {}
  
  var contentOverlay = $('#_div_isg_page_content_overlay');
  contentOverlay.height( '100%' );
  contentOverlay.width( '100%' );
  contentOverlay.css( 'position', 'absolute' );
  contentOverlay.css( 'background-color', '#FFFFFF' );
  contentOverlay.css( 'z-index', '999999' );
  contentOverlay.css( 'min-height', 'auto' );
  
  if ( contentOverlay.width() > 992 && !isFullWidth ) {
    contentOverlay.width( (contentOverlay.width() - 235) + 'px' );
    contentOverlay.offset({ top: 0, left: 235 });
  }
  else {
    contentOverlay.offset({ top: 0, left: 0 });
  }
  
  $('#_iframe_isg_page_content_overlay').remove();
  
  var frameLayer = document.createElement('iframe');
  frameLayer.id = '_iframe_isg_page_content_overlay';
  frameLayer.scrolling = 'no';
  frameLayer.style.border = '0';
  frameLayer.style.width = '100%';
  frameLayer.style.height = '100%';
  frameLayer.src = iframeURL;
  
  contentOverlay.html('');
  contentOverlay.append( frameLayer );  
  contentOverlay.show();
  
  // $( frameLayer.contentDocument ).ready( function() {
  //   setTimeout( function() { parent.$('#_iframe_isg_page_content_overlay').height( document.body.offsetHeight ); }, 1250);
  // });
}

function keyDownSearchMenu(keyCode) {
  if (keyCode == 27) {
    $('#_field_search_menu').val('');
    $('#_div_search_content').html('');
  }
}

function keyUpSearchMenu() {
  var _field_search = replaceCharEspecial( $('#_field_search_menu').val() ).toUpperCase();
  var _content_search = $('#_div_search_content');
  
  var maxValues = 10;
  
  var html = "";
  var count = 0;
  
  var emptyHTML = '';
  emptyHTML += '<div class="alert alert-warning text-center" style="margin: 20px 10px 20px 10px" id="search-box-msg">';
  emptyHTML += '  <%=user.getTermo("MSGBUSCARAPIDA")%>:<br/>';
  emptyHTML += '  <strong><%=user.getTermo("MENU")%></strong>;';
  emptyHTML += '</div>';
  
  // CARREGANDO DO SEARCH CONTENT
  Metronic.blockUI( { target: '#_ul_search_content', boxed: true, message: '<%=user.getTermo("SELECTBUSCANDO")%>' } );
  
  if (_field_search.length > 1) {      
    $('.search-menu-selector').each(function() {
      if ( replaceCharEspecial($(this).text()).toUpperCase().indexOf( _field_search ) > -1 ) {
        if ( count < maxValues && $(this).attr('onclick') ) {
          html += "<li class='search-box-item' onmousedown=\"" + $(this).attr('onclick') + "\"><a href='javascript:;' style='white-space: normal;'>" + $(this).text() + "</a></li>";
          count++;
        }
      }
      
      if (count >= maxValues) return false;
    });
   
    _content_search.html( (html ? "<li class='search-box-title'><p><%=SystemIcons.ICON_FOLDER_O + "&nbsp;&nbsp;" + user.getTermo("MENU")%></p></li>" : emptyHTML) + html );
    
    // BUSCANDO REGISTROS DO BANCO DE DADOS
    searchMenuDatabase( _field_search );
  }
  else {
    _content_search.html( emptyHTML );
  }
  
  Metronic.unblockUI('#_ul_search_content');
}

function searchMenuDatabase( searchText ) {
  void(0);
}

function formataCampo(campo, mascara, event) {
  var flagMascara;

  var exp = /\-|\.|\/|\(|\)| /g;
  var campoSoNumeros = campo.value.toString().replace(exp, "");

  var campoSoNumerosAux = mascara.replace(exp, "");
  
  campoSoNumeros = campoSoNumeros.substr(0, campoSoNumerosAux.length);

  var posicaoCampo = 0;
  var novoValorCampo = "";
  var tamanhoMascara = campoSoNumeros.length;
  
  if ( !event ) event = window.event;
  
  var k = event.keyCode || event.which;

  if (k != 8 && k != 46) {
    for (var i = 0; i <= tamanhoMascara; i++) {
      flagMascara  = ((mascara.charAt(i) == "-") || (mascara.charAt(i) == ".") || (mascara.charAt(i) == "/"));
      flagMascara  = flagMascara || ((mascara.charAt(i) == "(") || (mascara.charAt(i) == ")") || (mascara.charAt(i) == " "));
      if (flagMascara) {
        novoValorCampo += mascara.charAt(i);
        tamanhoMascara++;
      }
      else {
        novoValorCampo += campoSoNumeros.charAt(posicaoCampo);
        posicaoCampo++;
      }
    }
    
    campo.value = novoValorCampo;
  }
}

function mascaraTelefone(input, e) {
  if ( !e ) e = window.event;

  MascaraInteiro(e);
  
  var k = e.keyCode || e.which;
  var exp = /\-|\.|\/|\(|\)| /g;
  var str = input.value.toString().replace( exp, "" );
  
  var tam = str.length;
  if (tam >= 11 && k != 8 && k != 46) {
    e.keyCode = 0;
    e.wich = 0;
    e.preventDefault();
  }
  
  if(tam < 10 && e.type == 'blur'){
  	if(tam > 0){
  		alert('Telefone Inv·lido');
  	}
  	input.value = '';
  	return;
  }
  
  formataCampo(input, (tam >= 11?'(00) 00000-0000':'(00) 0000-0000'), e);
}

function ieVersion() {
     //Set defaults
     var value = {
         IsIE: false,
         TrueVersion: 0,
         ActingVersion: 0,
         CompatibilityMode: false
     };

     //Try to find the Trident version number
     var trident = navigator.userAgent.match(/Trident\/(\d+)/);
     if (trident) {
         value.IsIE = true;
         //Convert from the Trident version number to the IE version number
         value.TrueVersion = parseInt(trident[1], 10) + 4;
     }

     //Try to find the MSIE number
     var msie = navigator.userAgent.match(/MSIE (\d+)/);
     if (msie) {
         value.IsIE = true;
         //Find the IE version number from the user agent string
         value.ActingVersion = parseInt(msie[1]);
     } else {
         //Must be IE 11 in "edge" mode
         value.ActingVersion = value.TrueVersion;
     }

     //If we have both a Trident and MSIE version number, see if they're different
     if (value.IsIE && value.TrueVersion > 0 && value.ActingVersion > 0) {
         //In compatibility mode if the trident number doesn't match up with the MSIE number
         value.CompatibilityMode = value.TrueVersion != value.ActingVersion;
     }
     return value;
 }
 -->