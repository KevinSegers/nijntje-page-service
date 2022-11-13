package com.example.pageservice;

import com.example.pageservice.model.Page;
import com.example.pageservice.repository.PageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class PageControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageRepository pageRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    List<String> itemsPageOne = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"));
    List<String> itemsPageTwo = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"));


    @Test
    void whenGetAllPages_thenReturnJsonPage() throws Exception{
        Page pageOne = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");
        Page pageTwo = new Page(2, itemsPageTwo, false, "Nijntje in de speeltuin");
        List<Page> pageList= new ArrayList<>();
        pageList.add(pageOne);
        pageList.add(pageTwo);

        given(pageRepository.findAll()).willReturn(pageList);

        mockMvc.perform(get("/pages"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pageNumber", is(1)))
                .andExpect(jsonPath("$[0].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[0].seen", is(true)))
                .andExpect(jsonPath("$[0].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[1].pageNumber", is(2)))
                .andExpect(jsonPath("$[1].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"))))
                .andExpect(jsonPath("$[1].seen", is(false)))
                .andExpect(jsonPath("$[1].bookTitle", is("Nijntje in de speeltuin")));
    }

    @Test
    void givenBooktitle_whenGetPagesByBooktitle_thenReturnJsonPage() throws Exception {
        Page pageOne = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");
        Page pageTwo = new Page(2, itemsPageTwo, false, "Nijntje in de speeltuin");

        List<Page> pageList = new ArrayList<>();
        pageList.add(pageOne);
        pageList.add(pageTwo);

        given(pageRepository.findPagesByBookTitle("Nijntje in de speeltuin")).willReturn(pageList);


        mockMvc.perform(get("/pages/booktitle/{bookTitle}", "Nijntje in de speeltuin"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pageNumber", is(1)))
                .andExpect(jsonPath("$[0].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[0].seen", is(true)))
                .andExpect(jsonPath("$[0].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[1].pageNumber", is(2)))
                .andExpect(jsonPath("$[1].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"))))
                .andExpect(jsonPath("$[1].seen", is(false)))
                .andExpect(jsonPath("$[1].bookTitle", is("Nijntje in de speeltuin")));
    }

    @Test
    void givenBooktitleAndPageNumber_whenGetPageByBooktitleAndPageNumber_thenReturnJsonPage() throws Exception{
        Page page = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");

        given(pageRepository.findPageByBookTitleAndPageNumber("Nijntje in de speeltuin", 1)).willReturn(page);

        mockMvc.perform(get("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}", "Nijntje in de speeltuin", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$.seen", is(true)))
                .andExpect(jsonPath("$.bookTitle", is("Nijntje in de speeltuin")));
    }

    @Test
    void givenBooktitleAndPageNumber_whenGetItemsByBooktitleeAndPageNumber_thenReturnList() throws Exception{

        Page page = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");

        given(pageRepository.findPageByBookTitleAndPageNumber("Nijntje in de speeltuin", 1)).willReturn(page);

        mockMvc.perform(get("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}/items", "Nijntje in de speeltuin", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))));
    }

    @Test
    void givenBooktitleAndPageNumber_whenGetPagesUnseenByBooktitleeAndPageNumber_thenReturnDouble() throws Exception{
        Page pageOne = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");
        Page pageTwo = new Page(2, itemsPageTwo, false, "Nijntje in de speeltuin");

        List<Page> pageList = new ArrayList<>();
        pageList.add(pageOne);
        pageList.add(pageTwo);

        given(pageRepository.findPagesByBookTitle("Nijntje in de speeltuin")).willReturn(pageList);


        mockMvc.perform(get("/pages/booktitle/{bookTitle}/pagesunseen", "Nijntje in de speeltuin"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(0.50)));
    }




    @Test
    void givenBooktitle_whenSetPagesUnseen_thenReturnJsonPages() throws Exception{
        Page pageOne = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");
        Page pageTwo = new Page(2, itemsPageTwo, true, "Nijntje in de speeltuin");

        List<Page> pageList = new ArrayList<>();
        pageList.add(pageOne);
        pageList.add(pageTwo);

        given(pageRepository.findPagesByBookTitle("Nijntje in de speeltuin")).willReturn(pageList);

        mockMvc.perform(put("/pages/booktitle/{bookTitle}/setpagesunseen", "Nijntje in de speeltuin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pageNumber", is(1)))
                .andExpect(jsonPath("$[0].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[0].seen", is(false)))
                .andExpect(jsonPath("$[0].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[1].pageNumber", is(2)))
                .andExpect(jsonPath("$[1].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"))))
                .andExpect(jsonPath("$[1].seen", is(false)))
                .andExpect(jsonPath("$[1].bookTitle", is("Nijntje in de speeltuin")));
    }



    @Test
    void whenPostPage_thenReturnJsonPage() throws Exception {
        Page page = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");

        mockMvc.perform(post("/pages")
                        .content(mapper.writeValueAsString(page))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$.seen", is(true)))
                .andExpect(jsonPath("$.bookTitle", is("Nijntje in de speeltuin")));

    }

    @Test
    void givenPage_whenPutPage_thenReturnJsonPage() throws Exception {
        Page pageOne = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");

        given(pageRepository.findPageByBookTitleAndPageNumber("Nijntje in de speeltuin", 1)).willReturn(pageOne);

        Page updatedPageOne = new Page(1, itemsPageTwo, false, "Nijntje in de speeltuin");

        mockMvc.perform(put("/pages")
                        .content(mapper.writeValueAsString(updatedPageOne))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"))))
                .andExpect(jsonPath("$.seen", is(false)))
                .andExpect(jsonPath("$.bookTitle", is("Nijntje in de speeltuin")));
    }

    @Test
    void givenPage_whenDeletePage_thenStatusOk() throws Exception {
        Page pageToBeDeleted = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");

        given(pageRepository.findPageByBookTitleAndPageNumber("Nijntje in de speeltuin", 1)).willReturn(pageToBeDeleted);

        mockMvc.perform(delete("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}", "Nijntje in de speeltuin", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenNoExistingPage_whenDeletePage_thenStatusNotFound() throws Exception {
        given(pageRepository.findPageByBookTitleAndPageNumber("Nijntje in de speeltuin", 50)).willReturn(null);
        mockMvc.perform(delete("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}", "Nijntje in de speeltuin", 50)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}


