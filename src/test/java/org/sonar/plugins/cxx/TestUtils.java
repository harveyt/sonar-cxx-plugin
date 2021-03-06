/*
 * Sonar Cxx Plugin, open source software quality management tool.
 * Copyright (C) 2010 - 2011, Neticoa SAS France - Tous droits reserves.
 * Author(s) : Franck Bonin, Neticoa SAS France.
 *
 * Sonar Cxx Plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar Cxx Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar Cxx Plugin; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.cxx;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.plugins.cxx.utils.CxxUtils;

public class TestUtils{
  public static RuleFinder mockRuleFinder(){
    Rule ruleMock = Rule.create("", "", "");
    RuleFinder ruleFinder = mock(RuleFinder.class);
    when(ruleFinder.findByKey((String) anyObject(),
        (String) anyObject())).thenReturn(ruleMock);
    when(ruleFinder.find((RuleQuery) anyObject())).thenReturn(ruleMock);
    return ruleFinder;
  }

  public static File loadResource(String resourceName) {
    URL resource = TestUtils.class.getResource(resourceName);
    File resourceAsFile = null;
    try{
      resourceAsFile = new File(resource.toURI());
    } catch (URISyntaxException e) {
      System.out.println("Cannot load resource: " + resourceName);
    }
    
    return resourceAsFile;
  }

  public static Project mockProject() {
    File baseDir = loadResource("/org/sonar/plugins/cxx/");

    List<File> sourceFiles = new ArrayList<File>();
    sourceFiles.add(new File(baseDir, "sources/application/main.cpp"));
    sourceFiles.add(new File(baseDir, "sources/tests/SAMPLE-test.cpp"));
    sourceFiles.add(new File(baseDir, "sources/tests/SAMPLE-test.h"));
    sourceFiles.add(new File(baseDir, "sources/tests/main.cpp"));
    sourceFiles.add(new File(baseDir, "sources/utils/code_chunks.cpp"));
    sourceFiles.add(new File(baseDir, "sources/utils/utils.cpp"));

    List<InputFile> mainFiles = fromSourceFiles(sourceFiles);

    List<File> sourceDirs = new ArrayList<File>();
    sourceDirs.add(new File(baseDir, "sources"));

    ProjectFileSystem fileSystem = mock(ProjectFileSystem.class);
    when(fileSystem.getBasedir()).thenReturn(baseDir);
    when(fileSystem.getSourceCharset()).thenReturn(Charset.defaultCharset());
    when(fileSystem.getSourceFiles(mockCxxLanguage())).thenReturn(sourceFiles);
    when(fileSystem.mainFiles(CxxLanguage.KEY)).thenReturn(mainFiles);
    when(fileSystem.getSourceDirs()).thenReturn(sourceDirs);

    Project project = mock(Project.class);
    when(project.getFileSystem()).thenReturn(fileSystem);
    CxxLanguage lang = mockCxxLanguage();
    when(project.getLanguage()).thenReturn(lang);

    return project;
  }

  private static List<InputFile> fromSourceFiles(List<File> sourceFiles){
    List<InputFile> result = new ArrayList<InputFile>();
    for(File file: sourceFiles) {
      InputFile inputFile = mock(InputFile.class);
      when(inputFile.getFile()).thenReturn(new File(file, ""));
      result.add(inputFile);
    }
    return result;
  }

  public static CxxLanguage mockCxxLanguage(){
    return new CxxLanguage(mock(Configuration.class));
  }
}
