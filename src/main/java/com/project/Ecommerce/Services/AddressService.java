package com.project.Ecommerce.Services;

import ch.qos.logback.classic.Logger;
import com.project.Ecommerce.DTO.AddressDTO;
import com.project.Ecommerce.Models.Address;
import com.project.Ecommerce.Models.User;
import com.project.Ecommerce.Repo.AddressRepo;
import com.project.Ecommerce.Repo.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

     @Autowired
     private ModelMapper modelMapper;

     @Autowired
     private AddressRepo addressRepo;

     @Autowired
     private UserRepository userrepo;



    public ResponseEntity<?> createAddress(@Valid AddressDTO address1, User user) {

        List<Address>list=user.getAddresses();
        Address address=modelMapper.map(address1, Address.class);

        address.setUsers(user);
        Address Saveaddress= addressRepo.save(address);
        list.add(Saveaddress);
        user.setAddresses(list);

          userrepo.save(user);
          return ResponseEntity.ok(modelMapper.map(Saveaddress, AddressDTO.class));

    }

    public ResponseEntity<?> getAll() {

        List<AddressDTO> list =addressRepo.findAll().stream()
                .map(value->modelMapper.map(value, AddressDTO.class))
                .toList();

        return ResponseEntity.ok(list);

    }

    public ResponseEntity<?> getAddressById(Long addressId) {
        return ResponseEntity.ok(modelMapper.map(addressRepo.findById(addressId), AddressDTO.class));
    }

    public ResponseEntity<?> findaddresByUsername(String currentUserEmail) {

        User user=userrepo.findByEmail(currentUserEmail).orElseThrow(()-> new RuntimeException("User not found"));

        List<AddressDTO> list =user.getAddresses().stream()
                .map(value->modelMapper.map(value, AddressDTO.class))
                .toList();

        return ResponseEntity.ok(list);

    }

    public ResponseEntity<?> update(Long addressId, AddressDTO addressDTO) {
        Address address= addressRepo.findById(addressId).orElseThrow(()-> new RuntimeException("This Address is not available"));
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setBuildingName(addressDTO.getBuildingName());
        address.setCountry(addressDTO.getCountry());
        address.setState(addressDTO.getState());
        address.setPincode(addressDTO.getPincode());

        return ResponseEntity.ok(modelMapper.map(addressRepo.save(address), AddressDTO.class));

    }

    public ResponseEntity<?> delete(Long addressId) {
        Address address= addressRepo.findById(addressId).orElseThrow(()-> new RuntimeException("This Address is not available"));
        addressRepo.delete(address);
        return ResponseEntity.ok("Delete the address sucessfully");


    }
}
