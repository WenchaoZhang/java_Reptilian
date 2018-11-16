function DOMtoString(document_root) {
    
    node = document_root.firstChild;
    while (1) {
        if ( node.nodeType == Node.ELEMENT_NODE){
            return node.outerHTML;
            break;
        }else{
            node = node.nextSibling;
        }
    }
    return null;
}

chrome.extension.sendMessage({
    action: "getSource",
    source: { 
        'domString' : DOMtoString(document) , 
        'url' : document.URL,
        'title' : document.getElementsByTagName('title')[0].innerHTML
    }
});