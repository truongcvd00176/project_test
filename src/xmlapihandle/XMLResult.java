/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlapihandle;

import java.util.ArrayList;

/**
 *
 * @author daolinh
 */
public class XMLResult {
    
    private PageInformation pageInformation;
    private ArrayList<Member> listMember;

    public PageInformation getPageInformation() {
        return pageInformation;
    }

    public void setPageInformation(PageInformation pageInformation) {
        this.pageInformation = pageInformation;
    }

    public ArrayList<Member> getListMember() {
        return listMember;
    }

    public void setListMember(ArrayList<Member> listMember) {
        this.listMember = listMember;
    }
    
    
}
