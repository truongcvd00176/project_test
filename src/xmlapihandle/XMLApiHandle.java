/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlapihandle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author daolinh
 */
public class XMLApiHandle {

    public static ArrayList<Member> loadMember(int page, int limit) {
        ArrayList<Member> listMember = new ArrayList<>();
        try {
            System.out.println("Start getting xml content.");

            String url = "https://youtube-api-challenger.appspot.com/xml/members?page=" + page + "&limit=" + limit;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = builder.parse(con.getInputStream());

            // Chuẩn hoá, xoá bỏ các text node dư thừa.
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Member");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;

                long id = Long.parseLong(element.getAttribute("id"));
                String username = element.getElementsByTagName("UserName").item(0).getTextContent();
                String fullName = element.getElementsByTagName("FullName").item(0).getTextContent();
                String email = element.getElementsByTagName("Email").item(0).getTextContent();
                String password = element.getElementsByTagName("Password").item(0).getTextContent();
                String birthday = element.getElementsByTagName("Birthday").item(0).getTextContent();
                int gender = Integer.parseInt(element.getElementsByTagName("Gender").item(0).getTextContent());
                String avatar = element.getElementsByTagName("Avatar").item(0).getTextContent();
                int status = Integer.parseInt(element.getElementsByTagName("Status").item(0).getTextContent());

                Member member = new Member(id, username, fullName, email, password, birthday, gender, avatar, status);
                listMember.add(member);
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return listMember;
    }

    public static XMLResult loadMemberList(PageInformation oldPageInfor) {
        XMLResult result = new XMLResult();
        ArrayList<Member> listMember = new ArrayList<>();
        try {
            System.out.println("Start getting xml content.");

            String url = "https://youtube-api-challenger.appspot.com/xml/members?page=" + oldPageInfor.getPage() + "&limit=" + oldPageInfor.getLimit();

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = builder.parse(con.getInputStream());

            // Chuẩn hoá, xoá bỏ các text node dư thừa.
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Member");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;

                long id = Long.parseLong(element.getAttribute("id"));
                String username = element.getElementsByTagName("UserName").item(0).getTextContent();
                String fullName = element.getElementsByTagName("FullName").item(0).getTextContent();
                String email = element.getElementsByTagName("Email").item(0).getTextContent();
                String password = element.getElementsByTagName("Password").item(0).getTextContent();
                String birthday = element.getElementsByTagName("Birthday").item(0).getTextContent();
                int gender = Integer.parseInt(element.getElementsByTagName("Gender").item(0).getTextContent());
                String avatar = element.getElementsByTagName("Avatar").item(0).getTextContent();
                int status = Integer.parseInt(element.getElementsByTagName("Status").item(0).getTextContent());

                Member member = new Member(id, username, fullName, email, password, birthday, gender, avatar, status);
                listMember.add(member);
            }
            NodeList pageInfor = doc.getElementsByTagName("PageInformation");
            Node node = pageInfor.item(0);
            Element elementInfor = (Element) node;
            PageInformation pageInformation = new PageInformation();
            pageInformation.setTotalPage(Integer.parseInt(elementInfor.getElementsByTagName("TotalPage").item(0).getTextContent()));
            pageInformation.setTotalItem(Integer.parseInt(elementInfor.getElementsByTagName("TotalItem").item(0).getTextContent()));
            pageInformation.setLimit(Integer.parseInt(elementInfor.getElementsByTagName("Limit").item(0).getTextContent()));            
            pageInformation.setPage(Integer.parseInt(elementInfor.getElementsByTagName("Page").item(0).getTextContent()));
            result.setPageInformation(pageInformation);
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {            
            System.err.println(e.getMessage());
        }
        result.setListMember(listMember);
        return result;
    }

    public static void main(String[] args) {
        loadMember(1, 10);
    }

}
