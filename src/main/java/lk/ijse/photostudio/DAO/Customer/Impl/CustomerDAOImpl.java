package lk.ijse.photostudio.DAO.Customer.Impl;

import lk.ijse.photostudio.DAO.CrudUtil;
import lk.ijse.photostudio.DAO.Customer.CustomerDAO;
import lk.ijse.photostudio.Entity.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM customer");
        ArrayList<Customer> allCustomers = new ArrayList<>();
        while (rst.next()) {
            allCustomers.add(new Customer(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6))
            );
        }
        return allCustomers;
    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO customer VALUES (?,?,?,?,?)", entity.getId(), entity.getName(), entity.getAddress(), entity.getEmail(), entity.getContact());
    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE customer SET name=?, address=?, email=?, contact=? WHERE customer_id=?", entity.getName(), entity.getAddress(), entity.getEmail(), entity.getContact(), entity.getId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customer WHERE customer_id=?", id);
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT customer_id FROM customer WHERE customer_id=?", id);
        return rst.next();
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newId);
        } else {
            return "C001";
        }
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM customer WHERE customer_id=?", id);
        if (rst.next()) {
            return new Customer(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
        }
        return null;
    }
}