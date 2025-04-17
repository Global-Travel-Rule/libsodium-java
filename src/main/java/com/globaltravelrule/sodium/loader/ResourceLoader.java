/*
 * Copyright (c) 2025 Global Travel Rule • globaltravelrule.com
 * License that can be found in the LICENSE file.
 *
 * Author: Global Travel Rule developer
 * Created on: 2025/3/19 23:38
 */

package com.globaltravelrule.sodium.loader;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;


/**
 * Loads resources from a relative or absolute path
 * even if the file is in a JAR.
 */
public class ResourceLoader {

    private final Collection<PosixFilePermission> writePerms = new ArrayList<>(10);
    private final Collection<PosixFilePermission> readPerms = new ArrayList<>(10);

    ResourceLoader() {
        readPerms.add(PosixFilePermission.OWNER_READ);
        readPerms.add(PosixFilePermission.OTHERS_READ);
        readPerms.add(PosixFilePermission.GROUP_READ);

        writePerms.add(PosixFilePermission.OWNER_WRITE);
        writePerms.add(PosixFilePermission.OTHERS_WRITE);
        writePerms.add(PosixFilePermission.GROUP_WRITE);
    }

    /**
     * Copies a file into a temporary directory regardless of
     * if it is in a JAR or not.
     *
     * @param resourceClasspath A relative path to a file or directory
     *                          relative to the resources' folder.
     * @return The file or directory you want to load.
     * @throws IOException        If at any point processing of the resource file fails.
     * @throws URISyntaxException If not find the resource file.
     */
    public File copyToTempDirectory(String resourceClasspath, Class<?> outsideClass) throws IOException, URISyntaxException {
        // Create a "main" temporary directory in which
        // everything can be thrown in.
        File mainTempFile = createMainTempDirectory(resourceClasspath);

        // Standardized resource path (compatible with/and without/writing)
        String normalizedResource = resourceClasspath.startsWith("/")
                ? resourceClasspath.substring(1)
                : resourceClasspath;

        try (InputStream inputStream = getResourceAsStream(normalizedResource, outsideClass)) {
            if (inputStream == null) {
                throw new IOException("Resource not found in classpath: " + resourceClasspath);
            }

            Files.copy(inputStream, mainTempFile.toPath().toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            return mainTempFile;
        }
    }

    /**
     * Multi level attempt to obtain resource flow (compatible with various environments)
     */
    private InputStream getResourceAsStream(String resourcePath, Class<?> clazz) {
        // 1. Attempt context class loader (Spring Boot first)
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);
        // 2. Attempt the class loader of the current class
        if (is == null) {
            is = clazz.getClassLoader()
                    .getResourceAsStream(resourcePath);
        }
        // 3. Attempt system class loader
        if (is == null) {
            is = ClassLoader.getSystemResourceAsStream(resourcePath);
        }

        // 4. Finally, try to obtain it in absolute path mode
        if (is == null) {
            is = clazz.getResourceAsStream("/" + resourcePath);
        }
        return is;
    }

    /**
     * Creates the main temporary directory for resource-loader.
     *
     * @return A temp file that you can store temporary lib
     * @throws IOException Could not create a temporary file
     */
    public static File createMainTempDirectory(String relativeResourcePath) throws IOException {
        Path path = Files.createTempDirectory("sodium-lib");
        String[] relativeResourcePaths = relativeResourcePath.split("/");
        Path libDirPath = path.resolve(String.join(File.separator, Arrays.copyOf(relativeResourcePaths, relativeResourcePaths.length - 1)));
        File dir = libDirPath.toFile();
        // Create the required directories.
        Files.createDirectories(libDirPath);
        dir.deleteOnExit();
        Path libFilePath = libDirPath.resolve(relativeResourcePaths[relativeResourcePaths.length - 1]);
        if (Files.exists(libFilePath)) {
            Files.delete(libFilePath);
        }
        Files.createFile(libFilePath);
        return libFilePath.toFile();
    }

    /**
     * Sets permissions on a file or directory. This allows all users
     * to read, write and execute.
     *
     * @param file A file to set global permissions on
     * @throws IOException Could not set permissions
     * @see #setPermissions(File, Set)
     */
    public void setPermissions(File file) throws IOException {
        setPermissions(file, new HashSet<>());
    }

    /**
     * Sets a file or directory's permissions. @{code filePermissions} can be null, in that
     * case then global read, wrote and execute permissions will be set, so use
     * with caution.
     *
     * @param file            The file to set new permissions on.
     * @param filePermissions New permissions.
     * @return The file with correct permissions set.
     */
    public File setPermissions(File file, Set<PosixFilePermission> filePermissions) throws IOException {
        if (isPosixCompliant()) {
            // For posix set fine-grained permissions.
            if (filePermissions.isEmpty()) {
                Set<PosixFilePermission> perms = new HashSet<>();
                perms.add(PosixFilePermission.OWNER_READ);
                perms.add(PosixFilePermission.OWNER_WRITE);
                perms.add(PosixFilePermission.OWNER_EXECUTE);

                perms.add(PosixFilePermission.OTHERS_READ);
                perms.add(PosixFilePermission.OTHERS_WRITE);
                perms.add(PosixFilePermission.OTHERS_EXECUTE);

                perms.add(PosixFilePermission.GROUP_READ);
                perms.add(PosixFilePermission.GROUP_WRITE);
                perms.add(PosixFilePermission.GROUP_EXECUTE);
                filePermissions = perms;
            }
            Files.setPosixFilePermissions(file.toPath(), filePermissions);
        } else {
            // For non-posix like Windows find if any are true and
            // set the permissions accordingly.
            if (filePermissions.stream().anyMatch(readPerms::contains)) {
                if (!file.setReadable(true)) {
                    throw new SecurityException("Unable to modify file read permission: " + file.getPath());
                }
            } else if (filePermissions.stream().anyMatch(writePerms::contains)) {
                if (!file.setWritable(true)) {
                    throw new SecurityException("Unable to modify file write permission: " + file.getPath());
                }
            } else {
                if (!file.setExecutable(true)) {
                    throw new SecurityException("Unable to modify file execute permission: " + file.getPath());
                }
            }
        }
        return file;
    }

    /**
     * Mark the file or directory as "to be deleted".
     *
     * @param file The file or directory to be deleted.
     */
    public void requestDeletion(File file) throws IOException {
        if (isPosixCompliant()) {
            // The file can be deleted immediately after loading
            Files.delete(file.toPath());
        } else {
            // Don't delete until last file descriptor closed
            file.deleteOnExit();
        }
    }

    /**
     * Is the system we're running on Posix compliant?
     *
     * @return True if posix compliant.
     */
    protected boolean isPosixCompliant() {
        try {
            return FileSystems.getDefault()
                    .supportedFileAttributeViews()
                    .contains("posix");
        } catch (FileSystemNotFoundException | ProviderNotFoundException | SecurityException e) {
            return false;
        }
    }
}