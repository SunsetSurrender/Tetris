package main;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent;
import java.net.URL;

public class Sound {

    Clip musicClip;
    URL[] url = new URL[10];

    public Sound(){
        //wav files inserted in URL array audio path file
        url[0] = getClass().getResource("/main/resources/theme.wav");
        url[1] = getClass().getResource("/main/resources/collision.wav");
        url[2] = getClass().getResource("/main/resources/gameover.wav");
        url[3] = getClass().getResource("/main/resources/rotate.wav");
        url[4] = getClass().getResource("/main/resources/delete line.wav");

    }

    public void play(int i, boolean music) { // if music is playing then the boolean music is true, if a sound effect is playing then it's false

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);  // using this filepath to put the audio file in the clip object.
            Clip clip = AudioSystem.getClip();

            if(music){
                musicClip = clip;
            }

            clip.open(ais);
            clip.addLineListener(new LineListener() { //setting behaviour for when the audio is finished. Without it, it consumes more memory until it runs out.
                @Override
                public void update(LineEvent event) {
                    if(event.getType() == LineEvent.Type.STOP){
                        clip.close();
                    }
                }
            });
            ais.close();
            clip.start();

        }
        catch (Exception e) {

        }
    }
    // support method for looping music
    public void loop() {
        if (musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stop() {
            if (musicClip != null) {
                musicClip.stop();
                musicClip.close();
            }
    }
}
