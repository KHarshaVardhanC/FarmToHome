package com.ftohbackend.repositorytesting;


import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    private Customer customer;
//
//    @BeforeEach
//    public void setUp() {
//        customer = new Customer();
//        customer.setCustomerFirstName("Jane");
//        customer.setCustomerLastName("Doe");
//        customer.setCustomerEmail("jane.doe@example.com");
//        customer.setCustomerPassword("securePass123");
//        customer.setCustomerPhoneNumber("9876543210");
//        customer.setCustomerCity("Pune");
//        customer.setCustomerState("Maharashtra");
//        customer.setCustomerPlace("Kothrud");
//        customer.setCustomerPincode("411038");
//        customer.setCustomerIsActive(true);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        customerRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("Save customer test")
//    public void testSaveCustomer() {
//        Customer saved = customerRepository.save(customer);
//
//        assertThat(saved).isNotNull();
//        assertThat(saved.getCustomerId()).isNotNull();
//        assertThat(saved.getCustomerEmail()).isEqualTo("jane.doe@example.com");
//    }
//
//    @Test
//    @DisplayName("Find customer by ID test")
//    public void testFindById() {
//        Customer saved = customerRepository.save(customer);
//        Optional<Customer> result = customerRepository.findById(saved.getCustomerId());
//
//        assertThat(result).isPresent();
//        assertThat(result.get().getCustomerEmail()).isEqualTo("jane.doe@example.com");
//    }
//
//    @Test
//    @DisplayName("Find all customers test")
//    public void testFindAll() {
//        Customer customer1 = new Customer();
//        customer1.setCustomerFirstName("John");
//        customer1.setCustomerLastName("Smith");
//        customer1.setCustomerEmail("john.smith@example.com");
//        customer1.setCustomerPassword("password456");
//        customer1.setCustomerPhoneNumber("9123456789");
//        customer1.setCustomerCity("Delhi");
//        customer1.setCustomerState("Delhi");
//        customer1.setCustomerPlace("Connaught Place");
//        customer1.setCustomerPincode("110001");
//        customer1.setCustomerIsActive(true);
//
//        customerRepository.save(customer);
//        customerRepository.save(customer1);
//
//        List<Customer> all = customerRepository.findAll();
//
//        assertThat(all).isNotNull();
//        assertThat(all.size()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("Update customer email test")
//    public void testUpdateCustomer() {
//        Customer saved = customerRepository.save(customer);
//        saved.setCustomerEmail("updated.email@example.com");
//
//        Customer updated = customerRepository.save(saved);
//
//        assertThat(updated.getCustomerEmail()).isEqualTo("updated.email@example.com");
//    }
//
//    @Test
//    @DisplayName("Delete customer by ID test")
//    public void testDeleteCustomer() {
//        Customer saved = customerRepository.save(customer);
//
//        customerRepository.deleteById(saved.getCustomerId());
//
//        Optional<Customer> result = customerRepository.findById(saved.getCustomerId());
//
//        assertThat(result).isEmpty();
//    }
}

