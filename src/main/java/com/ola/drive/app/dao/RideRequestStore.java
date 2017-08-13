package com.ola.drive.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ola.drive.app.db.PostgreSQLJDBC;
import com.ola.drive.app.model.RideRequest;

/**
 * DAO layer class for CRUD operations on RideRequest table
 * 
 * @author aarishramesh
 *
 * Schema for table RideRequest
 *
 *   request_id     | integer                     | not null default nextval('riderequest_request_id_seq'::regclass)
     customer_id    | bigint                      | not null
     request_time   | timestamp without time zone | default now()
     start_location | text                        | 
     status         | smallint                    | default 0
     driver_id      | integer                     |
     start_time     | timestamp without time zone | default now()
     end_time       | timestamp without time zone | default now()
 */

public class RideRequestStore {
	private static final RideRequestStore INSTANCE = new RideRequestStore();

	public static RideRequestStore getInstance() {
		return INSTANCE;
	}

	public static void main(String[] args) throws SQLException {
		RideRequestStore requestStore = RideRequestStore.getInstance();
		//requestStore.markRideRequestComplete(1);
		for(RideRequest rideRequest : requestStore.getRideRequestsForDriverWithWaitingRequests(1)) 
			System.out.println(rideRequest);
	}

	public RideRequest addRideRequestFromCustomer(long customerId) throws SQLException {

		// Logic to insert ride request into database
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("INSERT INTO RideRequest VALUES (DEFAULT, ?, DEFAULT, NULL, DEFAULT, NULL, NULL, NULL);");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setLong(1, customerId);
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		RideRequest rideRequest = getRideRequestFromCustomerId(customerId);
		return rideRequest;
	}

	public RideRequest getRideRequest(long requestId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		RideRequest rideRequest = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from RideRequest where request_id = ?;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setLong(1, requestId);
			rs = pstmt.executeQuery();
			rideRequest = constructRideRequestFromRs(rs);
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}

		return rideRequest;
	}

	public RideRequest getRideRequestFromCustomerId(long customerId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		RideRequest rideRequest = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from RideRequest where customer_id = ?;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setLong(1, customerId);
			rs = pstmt.executeQuery();
			rideRequest = constructRideRequestFromRs(rs);
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}

		return rideRequest;
	}

	public RideRequest constructRideRequestFromRs(ResultSet rs) throws SQLException {
		RideRequest rideRequest = null;
		try {
			while(rs.next()) {
				rideRequest = new RideRequest();
				rideRequest.setRequestId(rs.getInt("request_id"));
				rideRequest.setCustomerId(rs.getLong("customer_id"));

				String requestTime = rs.getString("request_time");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(requestTime);
				rideRequest.setRequestTime(date.getTime());

				rideRequest.setRideLocaton(rs.getString("start_location"));
				rideRequest.setStatus(rs.getInt("status"));
				rideRequest.setDriverId(rs.getInt("driver_id"));

				String startTime = rs.getString("start_time");
				if (startTime != null) {
					date = sdf.parse(startTime);
					rideRequest.setStartTime(date.getTime());
				}

				String endTime = rs.getString("end_time");
				if (endTime != null) {
					date = sdf.parse(endTime);
					rideRequest.setEndTime(date.getTime());
				}
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return rideRequest;
	}

	public void addDriverToRideRequest(int requestId, int driverId) throws SQLException {
		// update driverId and status
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("update RideRequest set driver_id = ?, status = 1, start_time = DEFAULT, end_time = NULL where request_id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, driverId);
			pstmt.setInt(2, requestId);
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}

	public void markRideRequestComplete(int requestId) throws SQLException {
		// update status to done
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("update RideRequest set status = 2, end_time = DEFAULT where request_id = ?");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, requestId);
			pstmt.executeUpdate();
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
	}

	public List<RideRequest> getRideRequestsForDriverWithWaitingRequests(int driverId) throws SQLException {
		List<RideRequest> resultArr = new ArrayList<RideRequest>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from RideRequest where driver_id = ? or status = 0;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			pstmt.setInt(1, driverId);
			rs = pstmt.executeQuery();
			resultArr = constructRideRequestsFromResultSet(resultArr, rs);
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}
		return resultArr;
	}

	public List<RideRequest> getWaitingRideRequests() throws SQLException {
		List<RideRequest> resultArr = new ArrayList<RideRequest>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from RideRequest where status = 0;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			resultArr = constructRideRequestsFromResultSet(resultArr, rs);
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}

		return resultArr;
	}


	public List<RideRequest> getAllRideRequests() throws SQLException {
		List<RideRequest> resultArr = new ArrayList<RideRequest>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append ("select * from RideRequest;");
			connection = PostgreSQLJDBC.getInstance().connect();

			pstmt = connection.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			resultArr = constructRideRequestsFromResultSet(resultArr, rs);
		} catch(Exception e){
			throw new SQLException(e);
		} finally{
			if (pstmt != null) pstmt.close();
			if (connection != null) connection.close();
		}

		return resultArr;
	}

	private List<RideRequest> constructRideRequestsFromResultSet(List<RideRequest> resultArr, ResultSet rs) throws SQLException, ParseException {
		while(rs.next()) {
			RideRequest rideRequest = new RideRequest();
			rideRequest.setRequestId(rs.getInt("request_id"));
			rideRequest.setCustomerId(rs.getLong("customer_id"));

			String requestTime = rs.getString("request_time");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(requestTime);
			rideRequest.setRequestTime(date.getTime());

			rideRequest.setRideLocaton(rs.getString("start_location"));
			rideRequest.setStatus(rs.getInt("status"));
			rideRequest.setDriverId(rs.getInt("driver_id"));

			String startTime = rs.getString("start_time");
			if (startTime != null) {
				date = sdf.parse(startTime);
				rideRequest.setStartTime(date.getTime());
			}

			String endTime = rs.getString("end_time");
			if (endTime != null) {
				date = sdf.parse(endTime);
				rideRequest.setEndTime(date.getTime());
			}
			resultArr.add(rideRequest);
		}
		return resultArr;
	}
}
