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
public class FriendsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String uname, lname, friend = null;
    Statement st1, st2, st3, st4, st5, st6, st7 = null;
    ResultSet rs1, rs2, rs3, rs4, rs5, rs6 = null;
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
            out.println("<title>Servlet FriendsServlet</title>");            
            out.println("</head>");
            out.println("<body>");
           // out.println("<h1>Servlet FriendsServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
            
            friend = request.getParameter("addFriend");
            // out.println(friend);
            try{
                //Add friend list
                HttpSession session = request.getSession(false);
                uname = (String)session.getAttribute("fname");
                lname = (String)session.getAttribute("lname");
                out.println("Profile of " + uname + " " + lname);
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/socialnetwork", "root", "");
                
                st2 = conn.createStatement();
                rs2 = st2.executeQuery("select * from users where not firstName='"+uname+"' and not lastName='"+lname+"'");
                out.println("<form action = 'FriendsServlet'>");
                while(rs2.next()){
                    out.println("<input type = 'radio' name = 'addFriend' value = '"+rs2.getString(2)+"'>" + rs2.getString(2) + "<br>");
                }
                out.println("<input type = 'submit' name = 'AddFriend' value='Add Friend'>");
                out.println("</form>");
                // End add friend
                
                // Retrieve Id
                st4 = conn.createStatement();
                rs4 = st4.executeQuery("select id from users where firstName='"+uname+"' and lastName='"+lname+"'");
                while(rs4.next()){
                    id1 = Integer.parseInt(rs4.getString(1));
                    // out.println("User "+id1+"<br>");
                }
                
                st5 = conn.createStatement();
                rs5 = st5.executeQuery("select id from users where firstName='"+friend+"'");
                while(rs5.next()){
                    id2 = Integer.parseInt(rs5.getString(1));
                    // out.println("Friend "+id2+"<br>");
                }
                //End of Retrieve Id
                
                // Feed data
                preparedStatement = conn.prepareStatement("insert into friends(friendFrom,friendTo) values("+id1+","+id2+")");
                int i = preparedStatement.executeUpdate();
                out.println("<br>Friend added");
                // End of feed
                
                           
                // Show friends
                out.println("<br>Friends<br>");
                st3 = conn.createStatement();
                rs3 = st3.executeQuery("select * from friends where friendFrom="+id1+"");
                st7 = conn.createStatement();
                while(rs3.next()){
                    rs6 = st7.executeQuery("select * from users where id="+rs3.getString(3));
                    while(rs6.next()){
                        out.println(rs6.getString(2)+" "+rs6.getString(3)+"<br>");
                    }
                }                      
                // End friends
            }catch(Exception e){
                
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
        processRequest(request, response);
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
