package compiler;

import org.apache.commons.io.FileUtils;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;

public class SimpleInMemoryCompilator {

    private final static String SOURCE_FOLDER_PATH =
        System.getProperty("user.dir") + File.separator + "temp" + File.separator;

    public static byte[] compile(String sourceString, String classname) throws CompilationException {
        File sourceFolder = new File(SOURCE_FOLDER_PATH);
        byte[] sourceBytes = new byte[0];

        try {
            File source = save(sourceFolder, classname, sourceString);

            JavaCompiler compiler = getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, null, null);

            Iterable<? extends JavaFileObject> compilationUnits = stdFileManager.getJavaFileObjectsFromFiles(Collections.singletonList(source));

            JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics,
                Collections.singletonList("-parameters"), null, compilationUnits);
            Boolean isCompiled = task.call();

            if (!isCompiled) {
                StringBuilder errorMessage = new StringBuilder();
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                    System.out.printf("Error on line %d in %s. Message: %s", diagnostic.getLineNumber(), diagnostic.getSource(), diagnostic.getMessage(null));
                    errorMessage.append(String.format("Error on line %d. Message: %s\n", diagnostic.getLineNumber(),
                        diagnostic.getMessage(null)));
                }
                throw new CompilationException("Cannot loadContracts the file: " + source.getName() + "\n" + errorMessage.toString());
            }

            try {
                stdFileManager.close();
            } catch (IOException e) {
                System.out.println(e);
            }

            try {
                File classFile = new File(sourceFolder + File.separator + classname + ".class");
                sourceBytes = FileUtils.readFileToByteArray(classFile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }finally {
            for (File file : Objects.requireNonNull(sourceFolder.listFiles())) {
                file.delete();
            }
            sourceFolder.delete();
        }

        return sourceBytes;

    }

    private static JavaCompiler getSystemJavaCompiler() throws CompilationException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            String jdkPath = loadJdkPathFromEnvironmentVariables();
            System.setProperty("java.home", jdkPath);
            compiler = ToolProvider.getSystemJavaCompiler();
        }
        return compiler;
    }

    private static String loadJdkPathFromEnvironmentVariables() throws CompilationException {
        Pattern regexpJdkPath = Pattern.compile("jdk[\\d]\\.[\\d]\\.[\\d]([\\d._])");
        String jdkBinPath = Arrays.stream(System.getenv("Path").split(";"))
            .filter(it -> regexpJdkPath.matcher(it).find())
            .findFirst()
            .orElseThrow(() -> new CompilationException("Cannot compile the file. The java compiler has not been found, Java Development Kit should be installed."));
        return jdkBinPath.substring(0, jdkBinPath.length() - 4); // remove last 4 symbols "\bin"
    }

    private static File save(File sourceFolder, String classname, String sourceString) {
        byte[] sourceBytes = sourceString.getBytes();
        File sourceFile = new File(sourceFolder + File.separator + classname + ".java");
        try {
            FileUtils.writeByteArrayToFile(sourceFile, sourceBytes);
        } catch (IOException e) {
            System.out.println(e);
        }

        return sourceFile;
    }
}

