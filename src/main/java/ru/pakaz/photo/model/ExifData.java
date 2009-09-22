package ru.pakaz.photo.model;

import java.util.HashMap;
import java.util.Set;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExifData {
    private int height;
    private int width;
    private String CameraModel;
    
    public int depth = 0;
    public Logger logger = Logger.getLogger( ExifData.class );

    public int getHeight() {
        return this.height;
    }
    public void setHeight( int height ) {
        this.height = height;
    }
    public int getWidth() {
        return this.width;
    }
    public void setWidth( int width ) {
        this.width = width;
    }
    public String getCameraModel() {
        return this.CameraModel;
    }
    public void setCameraModel( String cameraModel ) {
        this.CameraModel = cameraModel;
    }
    
    public void parseExifTree( Node node ) {
        this.depth++;
        /*if( node.getNodeType() != Node.ELEMENT_NODE ) {
            return;
        }*/
        int type = node.getNodeType();
        String shift = String.format( "%"+ String.valueOf( this.depth*4 ) +"s", " " );
        String nodeName = node.getNodeName().trim();

        if( type == Node.ELEMENT_NODE ) {
//            this.logger.debug( shift +"Element type: Element" );
            this.logger.debug( shift + nodeName +" = "+ this.getValue( node ) );
            HashMap<String, String> attribs = this.getAttributes( node );
            if( attribs != null && attribs.size() > 0 ) {
                StringBuffer attributes = new StringBuffer();
                Set<String> keys = attribs.keySet();

                for( String key : keys ) {
                    attributes.append( key +":"+ attribs.get( key ) +"; " );
                }
                
                this.logger.debug( shift + attributes );
            }

            if( node.hasChildNodes() ) {
                NodeList children = node.getChildNodes();

                for( int i = 0; i < children.getLength(); i++ ) {
                    this.parseExifTree( children.item(i) );
                }
            }
        }
        else if( type == Node.ATTRIBUTE_NODE ) {
            this.logger.debug( shift +"Element type: Attribute" );
        }
        else if( type == Node.TEXT_NODE ) {
            this.logger.debug( shift +"Element type: Text" );
        }
        else if( type == Node.COMMENT_NODE ) {
            this.logger.debug( shift +"Element type: Comment" );
        }
        else {
            this.logger.debug( shift +"Element type: Other" );
        }

        this.depth--;
    }
    
    public HashMap<String, String> getAttributes( Node node ) {
        HashMap<String, String> values = new HashMap<String, String>();

        // Перебор атрибутов
        NamedNodeMap attrs = node.getAttributes();

        if( attrs == null ) {
            return null;
        }
        
        for( int i = 0; i < attrs.getLength(); i++ )
        {
            Node attrNode = attrs.item(i);
//          System.out.println( "Attribute "+ attrNode.getNodeName() +"="+ ((Attr)attrNode).getValue() );
            values.put( attrNode.getNodeName(), ((Attr)attrNode).getValue() );
        }
        return values;
    }
    
    public String getValue( Node node ) {
        NodeList children = node.getChildNodes();

        if( children.item(0) != null ) {
            return children.item(0).getNodeValue();
        }
        return null;
    }
}
