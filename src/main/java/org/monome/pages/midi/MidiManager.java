package org.monome.pages.midi;

import com.sun.media.sound.RealTimeSequencerProvider;

import javax.sound.midi.*;

public class MidiManager {

    private static MidiManager instance;

    public static MidiManager getInstance() {
        if (instance == null)
            instance = new MidiManager();

        return instance;
    }

    private Synthesizer synthesizer;
    private MidiChannel channel;

    private MidiManager() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channel = synthesizer.getChannels()[0];
        } catch (MidiUnavailableException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void noteOn(int note, int velocity) {
        channel.noteOn(note, velocity);
    }

    public void noteOff(int note, int velocity) {
        channel.noteOff(note);
    }

}
