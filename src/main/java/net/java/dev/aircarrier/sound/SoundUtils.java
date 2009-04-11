package net.java.dev.aircarrier.sound;

import java.net.URL;

import com.jme.scene.Node;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.RangedAudioTracker;
import com.jmex.audio.AudioTrack.TrackType;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class SoundUtils {

	public static AudioTrack createMusic(URL resource) {
	    // setup a music score for our demo
		AudioSystem audio = AudioSystem.getSystem();
	    AudioTrack music1 = createMusicBasic(resource);
	    audio.getMusicQueue().setRepeatType(RepeatType.ALL);
	    audio.getMusicQueue().setCrossfadeinTime(2.5f);
	    audio.getMusicQueue().setCrossfadeoutTime(2.5f);
	    audio.getMusicQueue().addTrack(music1);
	    audio.getMusicQueue().play();
	    
	    return music1;
	}
	
	public static RangedAudioTracker createSFXTracker(URL resource, Node track, float minRange, float maxRange, float maxVolume) {
	    // setup positional sounds in our scene
	    AudioTrack sfx = createSFXBasic(resource);
	    RangedAudioTracker tracker = new RangedAudioTracker(sfx, minRange, maxRange);
	    tracker.setToTrack(track);
	    tracker.setTrackIn3D(true);
	    tracker.setMaxVolume(maxVolume);  // set volume on the tracker as it will control fade in, etc.
	    return tracker;
	}    

	public static RangedAudioTracker createHeadSpaceTracker(URL resource, Node track, float minRange, float maxRange, float maxVolume) {
	    // setup positional sounds in our scene
	    AudioTrack sfx = createHeadSpaceBasic(resource);
	    RangedAudioTracker tracker = new RangedAudioTracker(sfx, minRange, maxRange);
	    tracker.setToTrack(track);
	    tracker.setTrackIn3D(true);
	    tracker.setMaxVolume(maxVolume);  // set volume on the tracker as it will control fade in, etc.
	    return tracker;
	}    

	public static AudioTrack createMusicBasic(URL resource) {
	    // Create a non-streaming, non-looping, relative sound clip.
	    AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, false);
	    sound.setType(TrackType.MUSIC);
	    sound.setRelative(true);
	    sound.setVolume(0.7f);
	    sound.setLooping(false);
	    return sound;
	}
	
	public static AudioTrack createSFXBasic(URL resource) {
	    // Create a non-streaming, looping, positional sound clip.
	    AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, false);
	    sound.setType(TrackType.POSITIONAL);
	    sound.setRelative(false);
	    sound.setLooping(true);
	    return sound;
	}

	public static AudioTrack createHeadSpaceBasic(URL resource) {
	    // Create a non-streaming, looping, headspace sound clip.
	    AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, false);
	    sound.setType(TrackType.HEADSPACE);
	    sound.setRelative(false);
	    sound.setLooping(true);
	    return sound;
	}

}
