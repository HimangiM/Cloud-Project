/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author himan
 */
public class MainServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String uname, lname, friend, usname, lsname = null;
    Statement st1, st2, st3, st4, st5, st6, st7 = null;
    ResultSet rs1, rs2, rs3, rs4, rs5, rs7 = null;
    int id1, id2;
    PreparedStatement preparedStatement = null;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MainServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            // out.println("<h1>Servlet MainServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
            
            uname = request.getParameter("uname");
            lname = request.getParameter("lname");
            // friend = request.getParameter("addFriend");
            try{
                // Welcome Page
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/socialnetwork", "root", "");
                if (uname == null && lname == null){
                    usname = request.getParameter("usname");
                    lsname = request.getParameter("lsname");
                    
                    st7 = conn.createStatement();
                    st7.executeUpdate("insert into users(firstName,lastName) values('"+usname+"','"+lsname+"')");
                    
                    uname = usname;
                    lname = lsname;
                }
                              
                HttpSession session = request.getSession();
                session.setAttribute("fname", uname);
                session.setAttribute("lname", lname);
                // out.println(session.getAttribute("fname"));                
                
                st1 = conn.createStatement();
                rs1 = st1.executeQuery("select * from users where firstName='"+uname+"' and lastName='"+lname+"'");
                while(rs1.next()){
                    out.println("Welcome "+uname+".<br>Choose some friends to begin!");
                }
                //Welcome ends here
                
                // Retrieve Id
                st4 = conn.createStatement();
                rs4 = st4.executeQuery("select id from users where firstName='"+uname+"' and lastName='"+lname+"'");
                while(rs4.next()){
                    id1 = Integer.parseInt(rs4.getString(1));
                    //out.println("User "+id1+"<br>");
                }
                // End retrieve
                
                //Add friend list
                st2 = conn.createStatement();
                rs2 = st2.executeQuery("select * from users where not firstName='"+uname+"' and not lastName='"+lname+"' and id not in (select friendTo from friends where friendFrom="+id1+")");
                out.println("<form action = 'FriendsServlet'>");
                while(rs2.next()){
                    out.println("<input type ='radio' name ='addFriend' value ='"+rs2.getString(2)+"'>" + rs2.getString(2) + "<br>");
                }
                out.println("<input type = 'submit' name =Submit' value='Add Friend'>");
                out.println("</form>");
                out.println("No friends to show!");
                // End add friend
                
                
//                
//                st5 = conn.createStatement();
//                rs5 = st5.executeQuery("select id from users where firstName='"+friend+"'");
//                while(rs5.next()){
//                    id2 = Integer.parseInt(rs4.getString(1));
//                    out.println("Friend "+id2+"<br>");
//                }
               
                
                // Feed data
//                preparedStatement = conn.prepareStatement("insert into friends(friendFrom,friendTo) values("+id1+","+id2+")");
//                int i = preparedStatement.executeUpdate();
//                out.println("<br>Friend added "+i+"<br>");
                // End of feed
                
                
                
                // Show friends
//                st3 = conn.createStatement();
//                rs3 = st3.executeQuery("select * from friends where friendFrom="+id1+"");
//                while(rs3.next()){
//                    out.println(rs3.getString(3));
//                }                      
                // End friends
            }catch(Exception e){
                out.println("Login Failed"+e);
            }
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
