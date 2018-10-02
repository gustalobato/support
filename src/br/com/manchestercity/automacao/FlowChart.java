
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class FlowChart {

	public class Box {
		public String x; // posição x em relação ao container
		public String y; // posição y em relação ao container
		public String code; // identificação do Box
		public String title; // texto do Box
		public String dataShape; // formato do Box
		protected String id; // id do elemento DOM

		public Box() {
			x = "0";
			y = "0";
			code = "";
			title = "";
			id = "";
			dataShape = "";
		}

		public Box(String pX, String pY, String pCod, String pTitle, String pDataShape) {
			x = pX;
			y = pY;
			code = pCod;
			title = pTitle;
			dataShape = pDataShape;
			id = "box" + code;
		}
	}

	public class Connector extends Box {
		public Connector(String pX, String pY, String pCod, String pTitle, String pDataShape) {
			super(pX, pY, pCod, pTitle, pDataShape);
			id = "connector" + code;
		}
	}

	public class Connection {
		public Box source;
		public Box target;
		public String text;
		public String cod;

		public static final int FLOWCHART = 0;
		public static final int BEZIER = 1;
		public static final int STRAIGHT = 2;

		public Connection() {
			source = null;
			target = null;
			text = "";
			cod = "";
		}

		public Connection(Box pSource, Box pTarget, String pText, String pCod) {
			source = pSource;
			target = pTarget;
			text = pText;
			cod = pCod;
		}
	}

	private ArrayList<Box> boxList;
	private ArrayList<Connector> connectorList;
	private ArrayList<Connection> connectionList;
	private String afterBoxMoveJs;
	private String afterAddBoxJs;
	private String boxJsDblclick;
	private String afterConnectorMoveJs;
	private String afterAddConnectorJs;
	private String connectorJsDblclick;
	private String afterConnectJs;
	private String connectionJsDblclick;
	private String containerJsDblclick;
	private String containerJsKeyup;
	private String width;
	private String height;
	private boolean connectionWithLabel;
	private int connectionStyle;

	public FlowChart() {
		boxList = new ArrayList<Box>();
		connectorList = new ArrayList<Connector>();
		connectionList = new ArrayList<Connection>();
		afterBoxMoveJs = "";
		afterAddBoxJs = "";
		boxJsDblclick = "";
		afterConnectorMoveJs = "";
		afterAddConnectorJs = "";
		connectorJsDblclick = "";
		afterConnectJs = "";
		connectionJsDblclick = "";
		containerJsDblclick = "";
		containerJsKeyup = "";
		width = "100%";
		height = "500px";
		connectionWithLabel = true;
		connectionStyle = 0;
	}

	public Box getBox(String pCod) {
		Box b = null;
		for (int i = 0; i < boxList.size(); i++) {
			b = (Box) boxList.get(i);
			if (pCod.equals(b.code)) {
				return b;
			}
		}
		return b;
	}

	public Connector getConnector(String pCod) {
		Connector c = null;
		for (int i = 0; i < connectorList.size(); i++) {
			c = (Connector) connectorList.get(i);
			if (pCod.equals(c.code)) {
				return c;
			}
		}
		return c;
	}

	public void setAfterBoxMoveJs(String afterBoxMoveJs) {
		this.afterBoxMoveJs = afterBoxMoveJs;
	}

	public void setAfterAddBoxJs(String afterAddBoxJs) {
		this.afterAddBoxJs = afterAddBoxJs;
	}

	public void setBoxJsDblclick(String boxJsDblclick) {
		this.boxJsDblclick = boxJsDblclick;
	}

	public void setAfterConnectorMove(String afterConnectorMoveJs) {
		this.afterConnectorMoveJs = afterConnectorMoveJs;
	}

	public void setAfterAddConnectorJs(String afterAddConnectorJs) {
		this.afterAddConnectorJs = afterAddConnectorJs;
	}

	public void setConnectorJsDblclick(String connectorJsDblclick) {
		this.connectorJsDblclick = connectorJsDblclick;
	}

	public void setConnectionJsDblclick(String connectionJsDblclick) {
		this.connectionJsDblclick = connectionJsDblclick;
	}

	public void setAfterConnectJs(String afterConnectJs) {
		this.afterConnectJs = afterConnectJs;
	}

	public void setContainerJsDblclick(String containerJsDblclick) {
		this.containerJsDblclick = containerJsDblclick;
	}

	public void setContainerJsKeyup(String containerJsKeyup) {
		this.containerJsKeyup = containerJsKeyup;
	}

	public void addBox(String pX, String pY, String pId, String pTitle, String pDataShape) {
		Box box = new Box(pX, pY, pId, pTitle, pDataShape);
		boxList.add(box);
	}

	public void addBox(String pX, String pY, String pId, String pTitle) {
		addBox(pX, pY, pId, pTitle, "");
	}

	public void addConnector(String pX, String pY, String pId, String pTitle, String pDataShape) {
		Connector con = new Connector(pX, pY, pId, pTitle, pDataShape);
		connectorList.add(con);
	}

	public void Connect(Box pSource, Box pTarget, String pText, String pCod) {
		Connection con = new Connection(pSource, pTarget, pText, pCod);
		connectionList.add(con);
	}

	public void setWidth(String pWidth) {
		width = pWidth;
	}

	public void setHeight(String pHeight) {
		height = pHeight;
	}

	public void setConnectionWithLabel(boolean pValue) {
		connectionWithLabel = pValue;
	}

	public void setConnectionStyle(int pValue) {
		connectionStyle = pValue;
	}

	public String toString() {
		StringBuffer fluxo = new StringBuffer();

		fluxo.append("<link rel='stylesheet' href='isg/plugins/jquery-jsPlumb/jsplumb.css' />\n");

		fluxo.append("<script type='text/javascript' src='metronic/global/plugins/jquery-1.11.0.min.js'></script>\n");
		fluxo.append("<script type='text/javascript' src='metronic/global/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js'></script> \n");
		fluxo.append("<script type='text/javascript' src='isg/plugins/jquery-jsPlumb/jquery.jsPlumb.js'></script>\n");
		fluxo.append("<script type='text/javascript' src='isg/plugins/jquery-jsPlumb/jquery.ui.touch-punch.min.js'></script>\n");

		fluxo.append("<div id='container' tabindex=0 style='width:" + width + ";height:" + height + ";'></div>\n");

		fluxo.append("<script>\n");

		fluxo.append("  var selectedItem = null;\n");
		fluxo.append("  var selectedConnection = null;\n");

		fluxo.append("  jsPlumb.importDefaults({\n");
		fluxo.append("    DragOptions : { cursor: 'pointer', zIndex: 2000 },\n");
		fluxo.append("    EndpointStyles : [{ fillStyle: '#4B77BE', radius: 4 }, { fillStyle: '#4B77BE', radius: 4 }],\n");
		fluxo.append("    MaxConnections : -1,\n");
		fluxo.append("    ConnectionOverlays : [\n");
		fluxo.append("      [ 'Arrow', { location : 1 } ]");
		if (connectionWithLabel) {
			fluxo.append(",\n");
			fluxo.append("      [ 'Label', { location: 0.5, id: 'label', cssClass: 'aLabel' }]");
		}
		fluxo.append("\n");
		fluxo.append("    ]\n");
		fluxo.append("  });\n\n");

		fluxo.append("  var connectorPaintStyle = {\n");
		fluxo.append("    lineWidth: 2,\n");
		fluxo.append("    strokeStyle: '#4B77BE',\n");
		fluxo.append("    joinstyle: 'round' \n");
		fluxo.append("  },\n\n");

		fluxo.append("  sourceEndpoint = {\n");
		if (connectionStyle == Connection.FLOWCHART) {
			fluxo.append("    connector: [ 'Flowchart', { stub: [10, 10], gap: 6, cornerRadius: 0, alwaysRespectStubs: true } ],\n");
		}
		else if (connectionStyle == Connection.BEZIER) {
			fluxo.append("    connector: [ 'Bezier', {curviness: 10} ],\n");
		}
		else if (connectionStyle == Connection.STRAIGHT) {
			fluxo.append("    connector: [ 'Straight', { stub: [0, 0], gap: 10} ],\n");
		}
		else {
			fluxo.append("    connector: [ 'Flowchart', { stub: [10, 10], gap: 10, cornerRadius: 5, alwaysRespectStubs: true } ],\n");
		}
		fluxo.append("    connectorStyle: connectorPaintStyle,\n");
		fluxo.append("    detachable: false,\n");
		fluxo.append("    dragOptions: {},\n");
		fluxo.append("    endpoint: 'Dot',\n");
		fluxo.append("    isSource: true \n");
		fluxo.append("  },\n\n");

		fluxo.append("  init = function(connection) {\n");
		fluxo.append("    if (connection.source !== null && connection.target !== null) {\n");
		fluxo.append("      afterConnect(connection);\n");
		fluxo.append("    }\n");
		fluxo.append("  };\n\n");

		fluxo.append("  jsPlumb.bind('connectionDragStop', function(connection) {\n");
		fluxo.append("    init(connection);\n");
		fluxo.append("  });\n\n");

		fluxo.append("  function afterConnect(pConnection) {\n");
		if (!afterConnectJs.equals("")) {
			fluxo.append(afterConnectJs);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  $(document).on('click', function(e) {\n");
		fluxo.append("    deselectItem();\n");
		fluxo.append("    deselectConnection();\n");
		fluxo.append("  });\n\n");

		fluxo.append("  $(document).on('mouseup', function(e) {\n");
		fluxo.append("    if (selectedItem !== null) {\n");
		fluxo.append("      var atualX = selectedItem.position().left;\n");
		fluxo.append("      var atualY = selectedItem.position().top;\n");
		fluxo.append("      if (selectedItem.attr('x') !== atualX || selectedItem.attr('y') !== atualY) {\n");
		fluxo.append("        selectedItem.attr('x', atualX);\n");
		fluxo.append("        selectedItem.attr('y', atualY);\n");
		fluxo.append("        if (selectedItem.attr('id').indexOf('connector') < 0) {\n");
		fluxo.append("          afterBoxMove($(selectedItem));");
		fluxo.append("        }\n");
		fluxo.append("        else {\n");
		fluxo.append("          afterConnectorMove($(selectedItem));\n");
		fluxo.append("        }\n");
		fluxo.append("      }\n");
		fluxo.append("    }\n");
		fluxo.append("  });\n\n");

		fluxo.append("  $('#container').on('dblclick', function(e) {\n");
		fluxo.append("    containerDblclick(e);\n");
		fluxo.append("  });\n");

		fluxo.append("  function containerDblclick(pEvent) {\n");
		if (!containerJsDblclick.equals("")) {
			fluxo.append(containerJsDblclick);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  $('#container').parent().keyup(function(e) {\n");
		fluxo.append("    containerKeyup(e.which);\n");
		fluxo.append("  });\n");

		fluxo.append("  function containerKeyup(pKeyCod) {\n");
		if (!containerJsKeyup.equals("")) {
			fluxo.append(containerJsKeyup);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  jsPlumb.bind('click', function(conn, originalEvent) {\n");
		fluxo.append("    originalEvent.stopPropagation();\n");
		fluxo.append("    selectConnection(conn);\n");
		fluxo.append("  });\n\n");

		fluxo.append("  jsPlumb.bind('dblclick', function(conn, originalEvent) {\n");
		fluxo.append("    originalEvent.stopPropagation();\n");
		fluxo.append("    connectionDblclick(conn, originalEvent);\n");
		fluxo.append("  });\n");

		fluxo.append("  function connectionDblclick(pConnection, pEvent) {\n");
		if (!connectionJsDblclick.equals("")) {
			fluxo.append(connectionJsDblclick);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  function selectItem(pItem) {\n");
		fluxo.append("    deselectItem();\n");
		fluxo.append("    deselectConnection();\n");
		fluxo.append("    $(document).focus();\n");
		fluxo.append("    selectedItem = pItem;\n");
		fluxo.append("    selectedItem.attr('oldClass', selectedItem.attr('currentClass'));\n");
		fluxo.append("    selectedItem.attr('currentClass', selectedItem.attr('currentClass') + 'Select');\n");
		fluxo.append("    selectedItem.switchClass(selectedItem.attr('oldClass'), selectedItem.attr('currentClass'), 0);\n");
		fluxo.append("    selectedItem.attr('oldData-shape', selectedItem.attr('currentData-shape'));\n");
		fluxo.append("    selectedItem.attr('currentData-shape', selectedItem.attr('currentData-shape') + 'Select');\n");
		fluxo.append("    selectedItem.attr('data-shape', selectedItem.attr('currentData-shape'));\n");
		fluxo.append("    selectedItem.hide().show();\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function deselectItem() {\n");
		fluxo.append("    if (selectedItem) {\n");
		fluxo.append("      selectedItem.switchClass(selectedItem.attr('currentClass'), selectedItem.attr('oldClass'), 0);\n");
		fluxo.append("      selectedItem.attr('currentClass', selectedItem.attr('oldClass'));\n");
		fluxo.append("      selectedItem.attr('currentData-shape', selectedItem.attr('oldData-shape'));\n");
		fluxo.append("      selectedItem.attr('data-shape', selectedItem.attr('currentData-shape'));\n");
		fluxo.append("      selectedItem.hide().show();\n");
		fluxo.append("      selectedItem = null;\n");
		fluxo.append("    }\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function selectConnection(pConnection) {\n");
		fluxo.append("    deselectItem();\n");
		fluxo.append("    deselectConnection();\n");
		fluxo.append("    $(document).focus();\n");
		fluxo.append("    selectedConnection = pConnection;\n");
		fluxo.append("    selectedConnection._jsPlumb.paintStyle.strokeStyle = '#D84A38';\n");
		fluxo.append("    selectedConnection.getOverlay('label').removeClass('aLabel');\n");
		fluxo.append("    selectedConnection.getOverlay('label').addClass('aLabelSelect');\n");
		fluxo.append("    selectedConnection.repaint();\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function deselectConnection() {\n");
		fluxo.append("    if (selectedConnection) {\n");
		fluxo.append("      selectedConnection._jsPlumb.paintStyle.strokeStyle = '#4B77BE';\n");
		fluxo.append("      selectedConnection.getOverlay('label').removeClass('aLabelSelect');\n");
		fluxo.append("      selectedConnection.getOverlay('label').addClass('aLabel');\n");
		fluxo.append("      selectedConnection.repaint();\n");
		fluxo.append("      selectedConnection = null;\n");
		fluxo.append("    }\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function removeConnection(pConn) {\n");
		fluxo.append("    jsPlumb.detach(pConn);\n");
		fluxo.append("    selectedConnection = null;\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function removeItemConnections(pItem) {\n");
		fluxo.append("    jsPlumb.detachAllConnections(pItem);\n");
		fluxo.append("  }\n");

		fluxo.append("  function removeItem(pItem) {\n");
		fluxo.append("    removeItemConnections(pItem);\n");
		fluxo.append("    pItem.remove();\n");
		fluxo.append("    selectedItem = null;\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function getItemText(pItem){\n");
		fluxo.append("    return $('.title', pItem).html();\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function setItemText(pItem, pText) {\n");
		fluxo.append("    $('.title', pItem).html(pText);\n");
		fluxo.append("  }\n\n");

		if (connectionWithLabel) {
			fluxo.append("  function setConnectionText(pConnection, pText) {\n");
			fluxo.append("    pConnection.getOverlay('label').setLabel(pText);\n");
			fluxo.append("  }\n\n");

			fluxo.append("  function getConnectionText(pConnection) {\n");
			fluxo.append("    return pConnection.getOverlay('label').getLabel();\n");
			fluxo.append("  }\n\n");

			fluxo.append("  function hideConnectionText(pConnection) {\n");
			fluxo.append("    pConnection.hideOverlay('label');\n");
			fluxo.append("  }\n\n");

			fluxo.append("  function showConnectionText(pConnection) {\n");
			fluxo.append("    pConnection.showOverlay('label');\n");
			fluxo.append("  }\n\n");
		}

		fluxo.append("  function addBox(pCod, pTitle, pX, pY, pDataShape) {\n");
		fluxo.append("    var newState = $('<div>')\n");
		fluxo.append("      .attr('id', 'box' + pCod)\n");
		fluxo.append("      .attr('cod', pCod)\n");
		fluxo.append("      .attr('x',pX)\n");
		fluxo.append("      .attr('y',pY);\n\n");

		fluxo.append("    if (pDataShape !== null && pDataShape !== '') {\n");
		fluxo.append("      newState.attr('data-shape', pDataShape)\n");
		fluxo.append("        .attr('CurrentData-shape', pDataShape)\n");
		fluxo.append("        .addClass('shape')\n");
		fluxo.append("        .attr('currentClass','shape');\n");
		fluxo.append("    }\n");
		fluxo.append("    else {\n");
		fluxo.append("      newState.attr('currentClass','box').addClass('box');\n");
		fluxo.append("    }\n\n");

		fluxo.append("    var title = $('<div>')\n");
		fluxo.append("      .addClass('title')\n");
		fluxo.append("      .text(pTitle);\n\n");

		fluxo.append("    newState.css({ 'top': pY, 'left': pX });\n\n");

		fluxo.append("    jsPlumb.makeTarget(newState, { anchor: 'Continuous' }, { detachable : false });\n");
		fluxo.append("    jsPlumb.makeSource(title, { parent: newState, anchor: 'Continuous' }, sourceEndpoint);\n");
		fluxo.append("    jsPlumb.draggable(newState, { containment: 'parent' });\n\n");

		// ADICIONADO PARA NÃO MOVER O BOX JUNTO COM A NOVA CONEXÃO
		fluxo.append("    title.mousedown(function(e) {\n");
		fluxo.append("      e.stopPropagation();\n");
		fluxo.append("    });\n\n");

		fluxo.append("    newState.click(function(e) {\n");
		fluxo.append("      selectItem($(this));\n");
		fluxo.append("      e.stopPropagation();\n");
		fluxo.append("    });\n\n");

		fluxo.append("    newState.mousedown(function(e) {\n");
		fluxo.append("      selectItem($(this));\n");
		fluxo.append("      e.stopPropagation();\n");
		fluxo.append("    });\n\n");

		fluxo.append("    newState.dblclick(function(e) {\n");
		fluxo.append("      boxDblclick($(this), e);\n");
		fluxo.append("      e.stopPropagation();\n");
		fluxo.append("    });\n\n");

		fluxo.append("    newState.append(title);\n");
		fluxo.append("    $('#container').append(newState);\n");
		fluxo.append("    afterAddBox(newState);\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function afterBoxMove(pBox) {\n");
		if (!afterBoxMoveJs.equals("")) {
			fluxo.append(afterBoxMoveJs);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  function afterAddBox(pBox) {\n");
		if (!afterAddBoxJs.equals("")) {
			fluxo.append(afterAddBoxJs);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  function boxDblclick(pBox, pEvent) {\n");
		if (!boxJsDblclick.equals("")) {
			fluxo.append(boxJsDblclick);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  function getBox(pCod){\n");
		fluxo.append("    return $('#box' + pCod);\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function addConnector(pCod, pDataShape, pTitle, pX, pY) {\n");
		fluxo.append("    var newState = $('<div>')\n");
		fluxo.append("      .attr('id', 'connector' + pCod)\n");
		fluxo.append("      .attr('cod', pCod)\n");
		fluxo.append("      .attr('x', pX)\n");
		fluxo.append("      .attr('y', pY)\n");
		fluxo.append("      .attr('data-shape', pDataShape)\n");
		fluxo.append("      .attr('CurrentData-shape', pDataShape)\n");
		fluxo.append("      .addClass('shape')\n");
		fluxo.append("      .attr('currentClass', 'shape');\n\n");

		fluxo.append("    var title = $('<div>').addClass('title').text(pTitle);\n\n");

		fluxo.append("    newState.css({ 'top': pY, 'left': pX });\n\n");

		fluxo.append("    if (pDataShape.indexOf('Triangle') > -1) {\n");
		fluxo.append("      pDataShape = 'Triangle';\n");
		fluxo.append("    }\n");
		fluxo.append("    else if (pDataShape.indexOf('Circle') > -1) {\n");
		fluxo.append("      pDataShape = 'Circle';\n");
		fluxo.append("    }\n");
		fluxo.append("    else {\n");
		fluxo.append("      pDataShape = 'Diamond';\n");
		fluxo.append("    }\n\n");

		fluxo.append("    jsPlumb.makeTarget(newState, { anchor:[ 'Perimeter', { shape:pDataShape } ] });\n");
		fluxo.append("    jsPlumb.makeSource(title, { parent: newState, anchor: [ 'Perimeter', { shape:pDataShape } ] }, sourceEndpoint);\n");
		fluxo.append("    jsPlumb.draggable(newState, { containment: 'parent' });\n\n");

		// ADICIONADO PARA NÃO MOVER O BOX JUNTO COM A NOVA CONEXÃO
		fluxo.append("    title.mousedown(function(e) {\n");
		fluxo.append("      e.stopPropagation();\n");
		fluxo.append("    });\n\n");

		fluxo.append("    newState.click(function(e) {\n");
		fluxo.append("      selectItem($(this));\n");
		fluxo.append("      e.stopPropagation();\n");
		fluxo.append("    });\n\n");

		fluxo.append("    newState.mousedown(function(e) {\n");
		fluxo.append("      selectItem($(this));\n");
		fluxo.append("      e.stopPropagation();\n");
		fluxo.append("    });\n\n");

		fluxo.append("    newState.dblclick(function(e) {\n");
		fluxo.append("      connectorDblclick($(this), e);\n");
		fluxo.append("      e.stopPropagation();\n");
		fluxo.append("    });\n");

		fluxo.append("    newState.append(title);\n");
		fluxo.append("    $('#container').append(newState);\n");
		fluxo.append("    afterAddConnector(newState);\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function afterConnectorMove(pConnector) {\n");
		if (!afterConnectorMoveJs.equals("")) {
			fluxo.append(afterConnectorMoveJs);
			fluxo.append("\n");
		}
		fluxo.append("  }\n");

		fluxo.append("  function afterAddConnector(pConnector) {\n");
		if (!afterAddConnectorJs.equals("")) {
			fluxo.append(afterAddConnectorJs);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  function connectorDblclick(pConnector, pEvent) {\n\n");
		if (!connectorJsDblclick.equals("")) {
			fluxo.append(connectorJsDblclick);
			fluxo.append("\n");
		}
		fluxo.append("  }\n\n");

		fluxo.append("  function getConnector(pCod) {\n");
		fluxo.append("    return $('#connector' + pCod);\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function getConnectionByCod(pCod) {\n");
		fluxo.append("    var cons = jsPlumb.getConnections();\n");
		fluxo.append("    for (var i = 0; i < cons.length; i++) {\n");
		fluxo.append("      if (cons[i].cod === pCod) {\n");
		fluxo.append("        return cons[i];\n");
		fluxo.append("      }\n");
		fluxo.append("    }\n");
		fluxo.append("    return null;\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function getConnectionById(pId) {\n");
		fluxo.append("    var cons = jsPlumb.getConnections();\n");
		fluxo.append("    for (var i = 0; i < cons.length; i++) {\n");
		fluxo.append("      if (cons[i].id === pId) {\n");
		fluxo.append("        return cons[i];\n");
		fluxo.append("      }\n");
		fluxo.append("    }\n");
		fluxo.append("    return null;\n");
		fluxo.append("  }\n\n");

		fluxo.append("  function connectItens(pSourceId, pTargetId, pCod, pText) {\n");
		fluxo.append("    var lcon = jsPlumb.connect({\n");
		fluxo.append("      source: pSourceId,\n");
		fluxo.append("      target: pTargetId \n");
		fluxo.append("    });\n");
		fluxo.append("    $(lcon).attr('cod', pCod);\n");
		if (connectionWithLabel) {
			fluxo.append("    if (pText !== '') {\n");
			fluxo.append("      setConnectionText(lcon, pText);\n");
			fluxo.append("    }\n");
			fluxo.append("    else {\n");
			fluxo.append("      hideConnectionText(lcon);\n");
			fluxo.append("    }\n");
		}
		fluxo.append("    return lcon;\n");
		fluxo.append("  }\n\n");

		fluxo.append("  jsPlumb.ready(function() {\n");
		for (int i = 0; i < boxList.size(); i++) {
			Box box = (Box) boxList.get(i);
			fluxo.append("  addBox(");
			fluxo.append(box.code);
			fluxo.append(",");
			fluxo.append("'" + box.title + "'");
			fluxo.append(",");
			fluxo.append(box.x);
			fluxo.append(",");
			fluxo.append(box.y);
			fluxo.append(",");
			fluxo.append("'" + box.dataShape + "'");
			fluxo.append(");\n");
		}

		for (int i = 0; i < connectorList.size(); i++) {
			Connector con = (Connector) connectorList.get(i);
			fluxo.append("  addConnector(");
			fluxo.append(con.code);
			fluxo.append(",");
			fluxo.append("'" + con.dataShape + "'");
			fluxo.append(",");
			fluxo.append("'" + con.title + "'");
			fluxo.append(",");
			fluxo.append(con.x);
			fluxo.append(",");
			fluxo.append(con.y);
			fluxo.append(");\n");
		}

		for (int i = 0; i < connectionList.size(); i++) {
			Connection con = (Connection) connectionList.get(i);
			fluxo.append("    connectItens('" + con.source.id + "','" + con.target.id + "','" + con.cod + "','" + con.text.replaceAll("\'", "&#39;") + "');");
		}
		fluxo.append("  });\n");
		fluxo.append("</script>\n");

		return fluxo.toString();
	}
}
