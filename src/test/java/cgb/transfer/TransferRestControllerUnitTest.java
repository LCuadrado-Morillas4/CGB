package cgb.transfer;

import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cgb.transfer.controller.TransferRestController;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.Transfer;
import cgb.transfer.service.TransferService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TransferRestController.class)
public class TransferRestControllerUnitTest {
	
	private static Transfer transfer;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private TransferService transferService;
	
	@BeforeAll
	public static void init() {
		transfer = new Transfer();
		transfer.setId(1l);
		transfer.setSourceAccountNumber("123456789");
		transfer.setDestinationAccountNumber("987654321");
		transfer.setAmount(40.0);
		transfer.setTransferDate(LocalDate.now());
		transfer.setDescription("Test");
	}
	
	@Test
	void testCreateTransfer() throws Exception {
		when(transferService.createTransferForBatch(
				Mockito.any(String.class),
				Mockito.any(String.class),
				Mockito.any(Double.class),
				Mockito.any(LocalDate.class),
				Mockito.any(String.class))).thenReturn(transfer);
		
		mockMvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(asJsonString(transfer)))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}
	
	@Test
	void testDeleteTransfer() throws Exception {
		when(transferService.deleteTransfer(1l)).thenReturn(transfer);
		
	    Long id = 1L;
	    mockMvc.perform(delete("/api/transfers")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(String.valueOf(id)))
	            .andExpect(status().isOk())
	            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
	            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
	            .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
