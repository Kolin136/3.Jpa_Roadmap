package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  @Test
  public void save() throws Exception {
    //given
    Item item = new Item();
    itemRepository.save(item);
    //when

    //then
  }



}