package fr.ans.api.sign.esignsante.psc.api.requests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Test des EndPoints offerts par l'API esignsante-psc.
 */

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan("fr.ans.api.sign.esignsante.psc.api")
public class DefaultApiIntegrationTest {

    /** The mock mvc. */
    @Autowired
    private MockMvc mockMvc;
	

    /**
     * Vérification de la liste des services disponibles.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Vérification de la liste des services disponibles.")
    public void rootGetTest() throws Exception {
    	final String body = "[\"/\"]";
        mockMvc.perform(get("/").accept("application/json"))
                .andExpect(status().isOk()).andExpect(content().json(body)).andDo(print());
    }
    
}
