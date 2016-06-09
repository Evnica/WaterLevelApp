package com.evnica.waterlevelwithgui;

import com.evnica.waterlevelwithgui.logic.Formatter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.time.LocalTime;

/**
 * Class: TimeSpinner
 * Version: 0.1
 * Created on 16.09.2015
 * Idea borrowed from: James_D
 * Description:
 */

class TimeSpinner extends Spinner<LocalTime>
{
    // Property containing the current editing mode:

    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS) ;

    private ObjectProperty<Mode> modeProperty() {
        return mode;
    }

    private void setMode(Mode mode) {
        modeProperty().set(mode);
    }

    private long lastPressProcessed = 0;

    private TimeSpinner(LocalTime time) {
        setEditable(true);

        StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>()
        {
            @Override
            public String toString(LocalTime time)
            {
                return Formatter.getTIME_FORMATTER_HHmm().format(time);
            }

            @Override
            public LocalTime fromString(String string)
            {
                String[] tokens = string.split(":");
                int hours = getIntField(tokens, 0);
                int minutes = getIntField(tokens, 1) ;
               // int seconds = getIntField(tokens, 2);
                int totalSeconds = (hours * 60 + minutes) * 60; //+ seconds ;
                return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60, 0); //seconds % 60
            }

            private int getIntField(String[] tokens, int index)
            {
                if (tokens.length <= index || tokens[index].isEmpty()) {
                    return 0 ;
                }
                return Integer.parseInt(tokens[index]);
            }
        };

        TextFormatter<LocalTime> textFormatter = new TextFormatter<>(localTimeConverter, LocalTime.now(), c ->
        {
            String newText = c.getControlNewText(); //:[0-9]{0,2}
            if (newText.matches("[0-9]{0,2}:[0-9]{0,2}")) {
                return c ;
            }
            return null ;
        });

        // The spinner value factory defines increment and decrement by
        // delegating to the current editing mode:
        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>()
        {
            {
                setConverter(localTimeConverter);
                setValue(time);
            }

            @Override
            public void decrement(int steps) {
                setValue(mode.get().decrement(getValue(), steps));
                mode.get().select(TimeSpinner.this);
            }

            @Override
            public void increment(int steps) {
                setValue(mode.get().increment(getValue(), steps));
                mode.get().select(TimeSpinner.this);
            }
        };

        this.setValueFactory(valueFactory);
        this.getEditor().setTextFormatter(textFormatter);

        // When the mode changes, select the new portion:
        mode.addListener((obs, oldMode, newMode) -> {newMode.select(this);}  );

        // Update the mode when the user interacts with the editor.
        // This is a bit of a hack, e.g. calling spinner.getEditor().positionCaret()
        // could result in incorrect state. Directly observing the caretPostion
        // didn't work well though; getting that to work properly might be
        // a better approach in the long run.
        this.getEditor().addEventHandler( InputEvent.ANY, e ->
                {
                    int hourSeparatorIndex = this.getEditor().getText().indexOf( ':' );
                    if ( e instanceof MouseEvent && e.getEventType().equals( MouseEvent.MOUSE_CLICKED ) )
                    {
                        int caretPos = this.getEditor().getCaretPosition();

                        if ( caretPos <= hourSeparatorIndex )
                        {
                            mode.set( Mode.HOURS );
                        } else
                        {
                            mode.set( Mode.MINUTES );
                        }
                    }
                    if ( e instanceof KeyEvent )
                    {
                        if ( ( ( KeyEvent ) e ).getCode().equals( KeyCode.LEFT ) )
                        {
                            this.setMode( Mode.HOURS );
                            this.getEditor().positionCaret( 0 );
                            Mode.HOURS.select( TimeSpinner.this );
                        }
                        else if ( ( ( KeyEvent ) e ).getCode().equals( KeyCode.UP ) )
                        {
                            if ((System.currentTimeMillis() - lastPressProcessed) > 500)
                            {
                                this.increment( 1 );
                                lastPressProcessed = System.currentTimeMillis();
                            }
                        }
                        else if ( ( ( KeyEvent ) e ).getCode().equals( KeyCode.RIGHT ) )
                        {
                            this.setMode( Mode.MINUTES );
                            this.getEditor().positionCaret( hourSeparatorIndex + 1 );
                            Mode.MINUTES.select( TimeSpinner.this );
                        }
                        else if ( ( ( KeyEvent ) e ).getCode().equals( KeyCode.DOWN ) && System.currentTimeMillis() - lastPressProcessed > 500 )
                        {
                            this.decrement(1);
                            lastPressProcessed = System.currentTimeMillis();
                        }
                    }
                }
            );
    }

    TimeSpinner() {
        this(LocalTime.now());
    }

    private enum Mode
    {
        HOURS
        {
            @Override
            LocalTime increment (LocalTime time, int steps)
            {
                return time.plusHours( steps );
            }
            @Override
            void select(TimeSpinner spinner)
            {
                int index = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(0, index);
            }
        },
        MINUTES
        {
            @Override
            LocalTime increment(LocalTime time, int steps)
            {
                return time.plusMinutes(steps);
            }
            @Override
            void select(TimeSpinner spinner)
            {
                int hrIndex = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(hrIndex+1, spinner.getEditor().getText().length());
            }
        };

        abstract LocalTime increment(LocalTime time, int steps);
        abstract void select(TimeSpinner spinner);
        LocalTime decrement(LocalTime time, int steps)
        {
            return increment(time, -steps);
        }

        @Override
        public String toString()
        {
            if (this == HOURS)
            {
                return "HOURS";
            }
            else
            {
                return "MINUTES";
            }
        }
    }
}
