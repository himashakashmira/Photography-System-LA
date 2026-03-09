package lk.ijse.photostudio.BO.Custom.impl;

import lk.ijse.photostudio.BO.Custom.CustomerBO;
import lk.ijse.photostudio.DAO.Customer.CustomerDAO;
import lk.ijse.photostudio.DAO.DAOFactory;
import lk.ijse.photostudio.DTO.CustomerDTO;
import lk.ijse.photostudio.Entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOimpl implements CustomerBO {

    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> entities = customerDAO.getAll();
        ArrayList<CustomerDTO> dtos = new ArrayList<>();
        for (Customer c : entities) {
            dtos.add(new CustomerDTO(c.getId(), c.getName(), c.getContact(), c.getEmail(), c.getAddress()));
        }
        return dtos;
    }

    @Override
    public boolean saveCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getEmail(), dto.getPhone()));
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getEmail(), dto.getPhone()));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }

    @Override
    public String generateNewCustomerId() throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewID();
    }
}
