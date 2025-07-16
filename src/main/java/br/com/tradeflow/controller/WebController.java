package br.com.tradeflow.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WebController {

    @GetMapping(path = "/")
    public String index() {
        return "external";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }

    @GetMapping(path = "/customers")
    public String customers(Principal principal, Model model) {
        List<Customer> customers = addCustomers();
        model.addAttribute("customers", customers);
        model.addAttribute("username", principal.getName());
        return "customers";
    }

    // add customers for demonstration
    public List<Customer> addCustomers() {

        List<Customer> list = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setAddress("1111 foo blvd");
        customer1.setName("Foo Industries");
        customer1.setServiceRendered("Important services");
        list.add(customer1);

        Customer customer2 = new Customer();
        customer2.setId(1L);
        customer2.setAddress("2222 bar street");
        customer2.setName("Bar LLP");
        customer2.setServiceRendered("Important services");
        list.add(customer2);

        Customer customer3 = new Customer();
        customer3.setId(1L);
        customer3.setAddress("33 main street");
        customer3.setName("Big LLC");
        customer3.setServiceRendered("Important services");
        list.add(customer3);
        return list;
    }

    @Data
    private class Customer {

        private long id;
        private String address;
        private String name;
        private String serviceRendered;
    }
}
