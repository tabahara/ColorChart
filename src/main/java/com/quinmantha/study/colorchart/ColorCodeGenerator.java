package com.quinmantha.study.colorchart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by tukahara on 16/06/09.
 */
public class ColorCodeGenerator {
    public static void main(String[] args) throws IOException {
	ColorCodeGenerator o = ColorCodeGenerator.newInstance();
	final int timeout = 10 * 1000;
	Document document = Jsoup.parse(new URL("https://material.google.com/style/color.html"), timeout);

	FileWriter out = new FileWriter("material_color.xml");
	o.process(out, document);
	out.close();
    }


    static public ColorCodeGenerator newInstance(){
        return new ColorCodeGenerator();
    }

    private ColorCodeGenerator(){
    }

    public void process(Writer out, Document document) throws IOException {
	Elements cg = document.getElementsByClass("color-group");
	out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        out.write("<resources>\n");
        for( Element e : cg ){
            processColorGroup(out, e);
        }
	out.write("</resources>\n");
    }

    private void processColorGroup(Writer out, Element e) throws IOException {
        Elements colors = e.getElementsByClass("color");

        String groupName = "";
        for( Element color : colors ){
            Elements es = color.getElementsByClass("name");
            if( es != null && es.size() > 0 ) {
                groupName = (es.get(0).html()).toLowerCase();
                groupName = groupName.replaceAll(" ","_");
            } else {
                // all colors in this group
                String shade = color.getElementsByClass("shade").get(0).text();
                shade = shade.toLowerCase();
                String hex = color.getElementsByClass("hex").get(0).text();
                hex = hex.toLowerCase();
                if( groupName.isEmpty() ){
                    out.write(String.format("    <color name=\"%s\">%s</color>\n", shade, hex));
                } else {
                    out.write(String.format("    <color name=\"%s_%s\">%s</color>\n", groupName, shade, hex));
                }
            }
        }
    }
}

