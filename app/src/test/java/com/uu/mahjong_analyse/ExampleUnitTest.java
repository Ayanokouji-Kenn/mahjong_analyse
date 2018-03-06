package com.uu.mahjong_analyse;

import com.uu.mahjong_analyse.base.MyApplication;
import com.uu.mahjong_analyse.data.PlayerRepository;
import com.uu.mahjong_analyse.data.local.MajongDatabase;
import com.uu.mahjong_analyse.data.local.PlayerDataSourceImpl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void delDb() throws Exception {
        final PlayerRepository repo = PlayerRepository.getInstance(
                PlayerDataSourceImpl.Companion.getInstance(
                        MajongDatabase.Companion.getInstance(
                                MyApplication.getInstance()).playerDao()));
//        repo.getPlayers().subscribe(new Consumer<List<Player>>() {
//            @Override
//            public void accept(List<Player> players) throws Exception {
//                repo.getPlayers()
//            }
//        })
    }
}