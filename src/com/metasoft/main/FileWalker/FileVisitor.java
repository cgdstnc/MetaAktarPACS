package com.metasoft.main.FileWalker;

import com.metasoft.main.DatabaseOps;
import com.metasoft.main.utils;
import org.dcm4che3.data.Attributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * Created by Cagdas Tunca on 11.04.2017.
 */
public class FileVisitor extends SimpleFileVisitor<Path> {
    public int gezilenDosya=0;
    private DatabaseOps db;

    public FileVisitor(DatabaseOps db) {
        this.db = db;
    }

    @Override
    public FileVisitResult visitFile(Path path,BasicFileAttributes basicFileAttributes) {

//        if (basicFileAttributes.isRegularFile()) {
//            System.out.println(path + " is a regular file with size " + basicFileAttributes.size());
//            try {
//                File f = path.toFile();
//                String[] attr = utils.readInstanceRelatedAttributes(f);
//                db.updateAktarimRow(attr[0],"DCM4CHEEAKT",Integer.valueOf(attr[1]),Integer.valueOf(attr[2]),Integer.valueOf(attr[3]));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            gezilenDosya++;
//        } /*else if (basicFileAttributes.isSymbolicLink()) {
//           // System.out.println(path + " is a symbolic link.");
//        } else {
//            //System.out.println(path + " is not a regular file or symbolic link.");
//        }*/
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException ioException) {
        //System.out.println(path + " visited.");
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException ioException) {
        System.err.println(ioException);
        return CONTINUE;
    }
}
