
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlapihandle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author daolinh
 */
public class ListMemberFrame extends JFrame {

    private PageInformation pageInformation;
    private final int WIDTH = 800;
    private final int HEIGHT = 450;
    // Làm việc với table.
    private JTable tableMember;
    private DefaultTableModel tableModel; // Quyết định dữ liệu ở trong bảng.
    private JScrollPane scrollPane; // Là khung để cho table vào.

    // Các nút điều hướng, chuyển trang.
    private JButton btnFirst, btnPrevious, btnGo, btnNext, btnLast;
    private JTextField txtCurrentPage;

    public ListMemberFrame() {
        super();
        initComponents();
    }

    // Hàm khởi tạo các component trong frame.
    private void initComponents() {
        pageInformation = new PageInformation();
        setTitle("Member List");
        setSize(WIDTH, HEIGHT);
        setLayout(null);

        // Khởi tạo thông tin table.
        // Khởi tạo model cho bảng, định nghĩa các cột, tiêu đề cột sẽ xuất hiện trong bảng.
        String[] columnNames = {"ID", "Full Name", "Username", "Email", "Status"}; // Mảng các chuỗi.

        Object[][] data = {};
        tableModel = new DefaultTableModel(data, columnNames); // Dữ liệu chưa có gì.
        // Xử lý việc lấy dữ liệu.
        tableMember = new JTable(tableModel); // Tạo ra table từ model.
        scrollPane = new JScrollPane(tableMember); // Đưa bảng vào khung.
        scrollPane.setBounds(100, 30, 600, 300); // Set vị trí và kích thước cho khung.
        add(scrollPane); // Đưa khung vào frame.

        btnFirst = new JButton("<<");
        btnPrevious = new JButton("<");
        btnGo = new JButton("GO");
        btnNext = new JButton(">");
        btnLast = new JButton(">>");
        txtCurrentPage = new JTextField();
        txtCurrentPage.setText(String.valueOf(pageInformation.getPage()));

        btnFirst.setBounds(285, 350, 30, 30);
        btnPrevious.setBounds(325, 350, 30, 30);
        txtCurrentPage.setBounds(365, 350, 30, 30);
        btnGo.setBounds(405, 350, 30, 30);
        btnNext.setBounds(445, 350, 30, 30);
        btnLast.setBounds(485, 350, 30, 30);

        btnGo.addActionListener(new PaginationHandle());
        btnFirst.addActionListener(new PaginationHandle());
        btnPrevious.addActionListener(new PaginationHandle());
        btnNext.addActionListener(new PaginationHandle());
        btnLast.addActionListener(new PaginationHandle());
        add(btnFirst);
        add(btnPrevious);
        add(txtCurrentPage);
        add(btnGo);
        add(btnNext);
        add(btnLast);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Load dữ liệu ra bảng.
        loadTableData();
    }

    private void loadTableData() {
        // Reset dữ liệu về 0 dòng.
        tableModel.setRowCount(0);
        // Load dữ liệu từ API.
        XMLResult result = XMLApiHandle.loadMemberList(pageInformation);
        pageInformation = result.getPageInformation();
        // Sử dụng vòng lặp để add thông tin vào dòng của bảng.
        for (Member mem : result.getListMember()) {
            // String[] columnNames = {"ID", "Full Name", "Username", "Email", "Status"}; // Mảng các chuỗi.
            // Tạo ra mảng chứa thông tin của một member.
            Object[] row = {mem.getId(), mem.getFullName(), mem.getUsername(), mem.getEmail(), mem.getStatus()}; // Mảng các chuỗi.                        
            // Add thông tin vào model.
            tableModel.addRow(row);
        }
        handlePaginateButton();        
    }

    public static void main(String[] args) {
        ListMemberFrame memberFrame = new ListMemberFrame();
        memberFrame.setVisible(true);
    }

    private void handlePaginateButton() {        
        if(pageInformation.getPage() <= 1) {
            btnFirst.setEnabled(false);            
            btnPrevious.setEnabled(false);
        }else {
            btnFirst.setEnabled(true);            
            btnPrevious.setEnabled(true);
        }
        if(pageInformation.getPage() == pageInformation.getTotalPage()){
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
        }else{
            btnNext.setEnabled(true);
            btnLast.setEnabled(true);
        }
    }

    class PaginationHandle implements ActionListener {

        @Override
        public synchronized void actionPerformed(ActionEvent e) {            
            if (e.getSource() == btnGo) {
                try {
                    int inputPage = Integer.parseInt(txtCurrentPage.getText());
                    if(inputPage == pageInformation.getPage()){
                        return;
                    }
                    if (inputPage > pageInformation.getTotalPage() || inputPage <= 0){
                        JOptionPane.showMessageDialog(null, "Ý thầy là sao?", "Có lỗi xảy ra", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    pageInformation.setPage(inputPage);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số", "Có lỗi xảy ra", JOptionPane.ERROR_MESSAGE);
                    return;
                }                
            } else if (e.getSource() == btnFirst) {
                pageInformation.setPage(1);                
            } else if (e.getSource() == btnPrevious) {
                if(pageInformation.getPage() > 1){
                    pageInformation.setPage(pageInformation.getPage() - 1);
                }                       
            } else if (e.getSource() == btnNext) {
                if(pageInformation.getPage() < pageInformation.getTotalPage()){
                    pageInformation.setPage(pageInformation.getPage() + 1);
                }                
            } else if (e.getSource() == btnLast) {
                pageInformation.setPage(pageInformation.getTotalPage());                
            }
            txtCurrentPage.setText(String.valueOf(pageInformation.getPage()));
            loadTableData();
        }

    }

}
