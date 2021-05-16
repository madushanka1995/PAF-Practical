package com;

import java.sql.*;

public class Payment {
	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/gb", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String readPayments() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			
			output = "<table class='table table-hover'><tr><th>product Name</th><th>payment Date</th><th>amount</th>"
					+ "<th>product Id</th><th>phone</th>"
					+ "<th>Update</th><th>Remove</th></tr>";

			String query = "select * from payments";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String paymentId = Integer.toString(rs.getInt("paymentId"));
				String productName = rs.getString("productName");
				String paymentDate = rs.getString("paymentDate");
				String amount = rs.getString("amount");
				String productId = rs.getString("productId");
				String phone = rs.getString("phone");
				
				// Add into the html table
				output += "<tr><td><input id='hidpaymentIdUpdate' name='hidpaymentIdUpdate' type='hidden' value='" + paymentId
						+ "'>" + productName + "</td>";
				output += "<td>" + paymentDate + "</td>";
				output += "<td>" + amount + "</td>";
				output += "<td>" + productId + "</td>";
				output += "<td>" + phone + "</td>";
				
				// buttons
				output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"
						+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-itemid='"
						+ paymentId + "'>" + "</td></tr>";
			}
			con.close();
			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the Payments.";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String insertPayments(String productName, String paymentDate, String amount,String productId, String phone) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for inserting.";
			}
			// create a prepared statement
			String query = " insert into payments(`paymentId`,`productName`,`paymentDate`,`amount`,`productId`,`phone`)"
					+ "values(?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, productName);
			preparedStmt.setString(3, paymentDate);
			preparedStmt.setString(4, amount);
			preparedStmt.setString(5, productId);
			preparedStmt.setString(6, phone);
			
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newPay = readPayments();
			output = "{\"status\":\"success\", \"data\": \"" +newPay+ "\"}";
			
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while inserting the Doctors.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String updatePayments(String paymentId,String productName, String paymentDate, String amount, String productId,String phone) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for updating.";
			}
			// create a prepared statement
			String query = "UPDATE payments SET productName=?,paymentDate=?,amount=?,productId=?,phone=? WHERE paymentId=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			
		
			preparedStmt.setString(1, productName);
			preparedStmt.setString(2, paymentDate);
			preparedStmt.setString(3, amount);
			preparedStmt.setString(4, productId);
			preparedStmt.setString(5, phone);
			preparedStmt.setInt(6, Integer.parseInt(paymentId));
	
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newPay =readPayments();
			output = "{\"status\":\"success\", \"data\": \"" +newPay+ "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while updating the doctor.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String deletePayments(String paymentId) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}

			// create a prepared statement
			String query = "delete from payments where paymentId=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(paymentId));
			// execute the statement
			preparedStmt.execute();
			con.close();
			String newPay = readPayments();
			output = "{\"status\":\"success\", \"data\": \"" +newPay + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while deleting the doctor.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}
}