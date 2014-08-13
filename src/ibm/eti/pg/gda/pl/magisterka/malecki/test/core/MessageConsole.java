package ibm.eti.pg.gda.pl.magisterka.malecki.test.core;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/*
 *  Create a simple console to display text messages.
 *
 *  Messages can be directed here from different sources. Each source can
 *  have its messages displayed in a different color.
 *
 *  Messages can either be appended to the console or inserted as the first
 *  line of the console
 *
 *  You can limit the number of lines to hold in the Document.
 */
public class MessageConsole {
    private JTextComponent textComponent;
    private Document document;
    private boolean isAppend;

    public MessageConsole(JTextComponent aTextComponent) {
        this(aTextComponent, true);
    }

    /*
     *  Use the text component specified as a simply console to display
     *  text messages.
     *
     *  The messages can either be appended to the end of the console or
     *  inserted as the first line of the console.
     */
    public MessageConsole(JTextComponent aTextComponent, boolean aIsAppend) {
        this.textComponent = aTextComponent;
        this.document = textComponent.getDocument();
        this.isAppend = aIsAppend;
        textComponent.setEditable(false);
    }

    /*
     *  Redirect the output from the standard output to the console
     *  using the default text color and null PrintStream
     */
    public void redirectOut() {
        redirectOut(null, null);
    }

    /*
     *  Redirect the output from the standard output to the console
     *  using the specified color and PrintStream. When a PrintStream
     *  is specified the message will be added to the Document before
     *  it is also written to the PrintStream.
     */
    public void redirectOut(Color textColor, PrintStream printStream) {
        ConsoleOutputStream cos =
                new ConsoleOutputStream(textColor, printStream);
        System.setOut(new PrintStream(cos, true));
    }

    /*
     *  Redirect the output from the standard error to the console
     *  using the default text color and null PrintStream
     */
    public void redirectErr() {
        redirectErr(null, null);
    }

    /*
     *  Redirect the output from the standard error to the console
     *  using the specified color and PrintStream. When a PrintStream
     *  is specified the message will be added to the Document before
     *  it is also written to the PrintStream.
     */
    public void redirectErr(Color textColor, PrintStream printStream) {
        ConsoleOutputStream cos =
                new ConsoleOutputStream(textColor, printStream);
        System.setErr(new PrintStream(cos, true));
    }

    /*
     *  Class to intercept output from a PrintStream and  add it to a Document.
     *  The output can optionally be redirected to a different PrintStream.
     *  The text displayed in the Document can be color coded to indicate
     *  the output source.
     */
    class ConsoleOutputStream extends ByteArrayOutputStream {
        private final String eol = System.getProperty("line.separator");
        private SimpleAttributeSet attributes;
        private PrintStream printStream;
        private StringBuffer buffer = new StringBuffer(80);
        private boolean isFirstLine;

        /*
         *  Specify the option text color and PrintStream
         */
        public ConsoleOutputStream(Color textColor, PrintStream aPrintStream) {
            super();
            if (textColor != null) {
                attributes = new SimpleAttributeSet();
                StyleConstants.setForeground(attributes, textColor);
            }

            this.printStream = aPrintStream;

            if (isAppend) {
                isFirstLine = true;
            }
        }

        /*
         *  Override this method to intercept the output text. Each line
         *  of text output will actually involve invoking this method twice:
         *
         *  a) for the actual text message
         *  b) for the newLine string
         *
         *  The message will be treated differently depending on whether
         *  the line will be appended or inserted into the Document
         */
        @Override
        public void flush() {
            String message = toString();

            if (message.length() == 0) {
                return;
            }

            if (isAppend) {
                handleAppend(message);
            } else {
                handleInsert(message);
            }
            reset();
        }

        /*
         *  We don't want to have blank lines in the Document. The first line
         *  added will simply be the message. For additional lines it will be:
         *
         *  newLine + message
         */
        private void handleAppend(String message) {
            //  This check is needed in case the text in the Document has been
            //  cleared. The buffer may contain the EOL string from
            //  the previous message.

            if (document.getLength() == 0) {
                buffer.setLength(0);
            }

            if (eol.equals(message)) {
                buffer.append(message);
            } else {
                buffer.append(message);
                clearBuffer();
            }

        }
        /*
         *  We don't want to merge the new message with the existing message
         *  so the line will be inserted as:
         *
         *  message + newLine
         */
        private void handleInsert(String message) {
                buffer.append(message);

            if (eol.equals(message)) {
                clearBuffer();
            }
        }

        /*
         *  The message and the newLine have been added to the buffer in the
         *  appropriate order so we can now update the Document and send the
         *  text to the optional PrintStream.
         */
        private void clearBuffer() {
            //  In case both the standard out and standard err are being
            //  redirected we need to insert a newline character for the first
            //  line only

            if (isFirstLine && document.getLength() != 0) {
                buffer.insert(0, "\n");
            }

            isFirstLine = false;
            String line = buffer.toString();

            try {
                if (isAppend) {
                    int offset = document.getLength();
                    document.insertString(offset, line, attributes);
                    textComponent.setCaretPosition(document.getLength());
                } else {
                    document.insertString(0, line, attributes);
                    textComponent.setCaretPosition(0);
                }
            } catch (BadLocationException ble) {
                Logger.getLogger(MessageConsole.class.getName()).
                        log(Level.SEVERE, null, ble);
            }

            if (printStream != null) {
                printStream.print(line);
            }

            buffer.setLength(0);
        }
    }
}
