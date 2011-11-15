package org.monome.pages

import org.monome.pages.time.MusicalDuration
import org.monome.pages.time.MusicalScheduler
import javax.sound.midi.MidiDevice
import javax.sound.midi.spi.MidiDeviceProvider
import javax.sound.midi.MidiSystem
import com.sun.media.sound.RealTimeSequencerProvider
import javax.sound.midi.MidiMessage
import javax.sound.midi.ShortMessage
import org.monome.pages.midi.MidiManager

abstract class ScriptBase extends Script {

    static {
        Number.metaClass {
            getTh {-> new MusicalDuration(1, delegate)}
            getNd {-> delegate.th}
        }
    }

    MusicalDuration md(num, den) {
        new MusicalDuration(num, den)
    }

    def whole = 1.th
    def half = 2.th
    def qtr = 4.th

    def mm = MidiManager.getInstance()

    def every(MusicalDuration duration, Closure closure) {
        MusicalScheduler.getInstance().schedule(duration, closure)
    }

    def noteon(int note, int velocity) {
        mm.noteOn(note, velocity)
    }

    def noteoff(int note, int velocity) {
        mm.noteOff(note, velocity)
    }

    def once(MusicalDuration delay, Closure closure) {
        def first = true
        def task = null
        task = MusicalScheduler.getInstance().schedule(delay) {
            if (first) {
                first = false
            } else {
                closure.call()
                task.stop()
            }
        }
    }

    def play(int note, int velocity, MusicalDuration duration) {
        noteon(note, velocity)
        once(duration) {
            noteoff(note, velocity)
        }
    }
}
