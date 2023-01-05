package fr.lernejo.fileinjector;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

    @Test
    void main_terminates_before_5_sec() {
        assertTimeoutPreemptively(
            Duration.ofSeconds(5L),
            () -> Launcher.main(new String[]{}));
    }

   /* @Test
    void send_file_content_to_queue(){
        assertDoesNotThrow(
            () -> Launcher.main(new String[]{"/Users/xavierkzan/Documents/Cours /3PROJ/video_game_search_engine/file-injector/src/test/resources/games.json"})
            );
    }*/
}
