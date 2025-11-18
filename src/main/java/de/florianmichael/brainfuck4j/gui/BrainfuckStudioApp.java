/*
 * This file is part of Brainfuck4J - https://github.com/FlorianMichael/Brainfuck4J
 * Copyright (C) 2021-2025 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.florianmichael.brainfuck4j.gui;

import de.florianmichael.brainfuck4j.Brainfuck4J;
import de.florianmichael.brainfuck4j.dialect.DialectType;
import de.florianmichael.brainfuck4j.instruction.Instruction;
import de.florianmichael.brainfuck4j.memory.AbstractMemory;
import de.florianmichael.brainfuck4j.memory.MemoryType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class BrainfuckStudioApp extends JFrame {

    private final RSyntaxTextArea editor = new RSyntaxTextArea();
    private final JTextArea outputArea = new JTextArea();
    private final JTextArea inputArea = new JTextArea();
    private final JComboBox<DialectType> dialectBox = new JComboBox<>(DialectType.values());
    private final JComboBox<MemoryType> memoryBox = new JComboBox<>(MemoryType.values());
    private final JTextField memorySizeField = new JTextField("30000", 8);
    private final JLabel statusLabel = new JLabel("Ready");
    private final InstructionsTableModel instructionsModel = new InstructionsTableModel();

    private JButton runButton;
    private JButton stopButton;
    private SwingWorker<Void, String> currentWorker;
    private PipedOutputStream liveOutput;

    public BrainfuckStudioApp() {
        super("Brainfuck4J Studio");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout());

        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        editor.setCodeFoldingEnabled(true);

        add(buildTopPanel(), BorderLayout.NORTH);

        final JSplitPane vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildEditorPane(), buildOutputPane());
        vertical.setResizeWeight(0.5);
        add(vertical, BorderLayout.CENTER);

        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildTopPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Dialect:"));
        panel.add(dialectBox);
        panel.add(new JLabel("Memory:"));
        panel.add(memoryBox);
        panel.add(new JLabel("Size:"));
        panel.add(memorySizeField);

        runButton = new JButton(new AbstractAction("Run") {
            @Override
            public void actionPerformed(ActionEvent e) {
                runProgram();
            }
        });
        panel.add(runButton);

        stopButton = new JButton(new AbstractAction("Stop") {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelExecution();
            }
        });
        stopButton.setEnabled(false);
        panel.add(stopButton);

        final JButton openButton = new JButton("Open");
        openButton.addActionListener(e -> openFile());
        panel.add(openButton);

        final JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveFile());
        panel.add(saveButton);

        final JButton convertButton = new JButton("Convert...");
        convertButton.addActionListener(e -> new DialectConverterDialog(this).setVisible(true));
        panel.add(convertButton);
        return panel;
    }

    private Component buildEditorPane() {
        final JSplitPane editorSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new RTextScrollPane(editor), new JScrollPane(inputArea));
        editorSplit.setResizeWeight(0.8);
        inputArea.setBorder(BorderFactory.createTitledBorder("Input Stream (optional)"));
        return editorSplit;
    }

    private Component buildOutputPane() {
        final JPanel panel = new JPanel(new BorderLayout());
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        final JTable instructionsTable = new JTable(instructionsModel);
        instructionsTable.setFillsViewportHeight(true);

        final JScrollPane tableScroll = new JScrollPane(instructionsTable);
        tableScroll.setPreferredSize(new Dimension(400, 150));
        panel.add(tableScroll, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildStatusBar() {
        final JPanel status = new JPanel(new BorderLayout());
        status.add(statusLabel, BorderLayout.CENTER);
        return status;
    }

    private void runProgram() {
        if (currentWorker != null) {
            return;
        }

        outputArea.setText("");
        instructionsModel.setInstructions(new ArrayList<>());
        statusLabel.setText("Running...");
        runButton.setEnabled(false);
        stopButton.setEnabled(true);

        try {
            liveOutput = new PipedOutputStream();
            final Thread readerThread = createReaderThread();
            readerThread.start();
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to capture live output: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            resetExecutionState();
            return;
        }

        final String program = editor.getText();
        DialectType dialect = (DialectType) dialectBox.getSelectedItem();
        MemoryType memory = (MemoryType) memoryBox.getSelectedItem();
        int size;
        try {
            size = Integer.parseInt(memorySizeField.getText());
        } catch (NumberFormatException e) {
            size = 30000;
        }
        final byte[] stdin = inputArea.getText().getBytes(java.nio.charset.StandardCharsets.UTF_8);

        String normalizedProgram = program;
        if (dialect == null) {
            dialect = DialectType.BRAINFUCK;
        }
        if (dialect != DialectType.BRAINFUCK) {
            normalizedProgram = dialect.convert(program, DialectType.BRAINFUCK);
        }

        if (memory == null) {
            memory = MemoryType.INTEGER;
        }
        if (size <= 0) {
            size = 30000;
        }

        MemoryType finalMemory = memory;
        int finalSize = size;
        String finalProgram = normalizedProgram;

        TeeOutputStream teeOutput = new TeeOutputStream(liveOutput);

        currentWorker = new SwingWorker<>() {
            private List<Instruction> instructions;
            private long elapsedMillis;

            @Override
            protected Void doInBackground() {
                try (final ByteArrayInputStream in = new ByteArrayInputStream(stdin);
                     final PrintStream out = new PrintStream(teeOutput, true, StandardCharsets.UTF_8)
                ) {
                    final AbstractMemory mem = finalMemory.create(finalSize);

                    final long start = System.currentTimeMillis();
                    instructions = Brainfuck4J.INSTANCE.run(in, out, mem, finalProgram);
                    elapsedMillis = System.currentTimeMillis() - start;

                    final String captured = teeOutput.getCaptured();
                    if (captured != null && !captured.isEmpty()) {
                        publish(captured);
                    }
                } catch (Throwable throwable) {
                    publish("\n[ERROR] " + throwable.getMessage());
                }
                return null;
            }

            @Override
            protected void process(final List<String> chunks) {
                for (final String chunk : chunks) {
                    publishOutput(chunk);
                }
            }

            @Override
            protected void done() {
                resetExecutionState();
                if (instructions != null) {
                    instructionsModel.setInstructions(instructions);
                    statusLabel.setText("Finished in " + elapsedMillis + " ms | Instructions: " + instructions.size());
                } else {
                    statusLabel.setText("Execution finished with errors");
                }
            }
        };
        currentWorker.execute();
    }

    private Thread createReaderThread() throws IOException {
        final PipedInputStream liveInput = new PipedInputStream(liveOutput);
        final Thread readerThread = new Thread(() -> {
            try (liveInput) {
                byte[] buffer = new byte[256];
                int read;
                while ((read = liveInput.read(buffer)) != -1) {
                    String chunk = new String(buffer, 0, read, java.nio.charset.StandardCharsets.UTF_8);
                    publishOutput(chunk);
                }
            } catch (IOException ignored) {
            }
        }, "bf-output-reader");
        readerThread.setDaemon(true);
        return readerThread;
    }

    private void publishOutput(final String text) {
        SwingUtilities.invokeLater(() -> outputArea.append(text));
    }

    private void cancelExecution() {
        if (currentWorker != null) {
            currentWorker.cancel(true);
            resetExecutionState();
            statusLabel.setText("Execution cancelled");
        }
    }

    private void resetExecutionState() {
        if (liveOutput != null) {
            try {
                liveOutput.close();
            } catch (final IOException ignored) {
            }
            liveOutput = null;
        }

        runButton.setEnabled(true);
        stopButton.setEnabled(false);
        currentWorker = null;
    }

    private void openFile() {
        final JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            final Path path = chooser.getSelectedFile().toPath();
            try {
                editor.setText(Files.readString(path));
            } catch (final IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        final JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            final Path path = chooser.getSelectedFile().toPath();
            try {
                Files.writeString(path, editor.getText());
            } catch (final IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class InstructionsTableModel extends AbstractTableModel {

        private final String[] columns = {"Index", "Type", "Count"};
        private List<Instruction> data = new ArrayList<>();

        public void setInstructions(final List<Instruction> instructions) {
            this.data = instructions;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Instruction instruction = data.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> rowIndex;
                case 1 -> instruction.type;
                case 2 -> instruction.count;
                default -> "";
            };
        }
    }

    private static class TeeOutputStream extends OutputStream {
        private final PipedOutputStream piped;
        private final ByteArrayOutputStream mirror = new ByteArrayOutputStream();

        TeeOutputStream(final PipedOutputStream piped) {
            this.piped = piped;
        }

        @Override
        public void write(int b) throws IOException {
            piped.write(b);
            mirror.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            piped.write(b, off, len);
            mirror.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            piped.flush();
            mirror.flush();
        }

        @Override
        public void close() throws IOException {
            piped.close();
            mirror.close();
        }

        public String getCaptured() {
            return mirror.toString(java.nio.charset.StandardCharsets.UTF_8);
        }
    }

}
