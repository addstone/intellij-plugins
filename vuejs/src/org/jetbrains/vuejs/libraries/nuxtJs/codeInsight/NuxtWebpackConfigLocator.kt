// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.vuejs.libraries.nuxtJs.codeInsight

import com.intellij.javascript.nodejs.NodeModuleDirectorySearchProcessor
import com.intellij.javascript.nodejs.NodeModuleSearchUtil
import com.intellij.javascript.nodejs.PackageJsonData
import com.intellij.javascript.nodejs.packageJson.PackageJsonFileManager
import com.intellij.lang.javascript.buildTools.webpack.WebpackConfigLocator
import com.intellij.openapi.project.Project
import org.jetbrains.vuejs.libraries.nuxtJs.NUXT_PKG

class NuxtWebpackConfigLocator : WebpackConfigLocator {
  override fun detectConfig(project: Project): String? =
    PackageJsonFileManager.getInstance(project).validPackageJsonFiles
      .asSequence()
      .filter { it.isValid && PackageJsonData.getOrCreate(it).isDependencyOfAnyType(NUXT_PKG) }
      .mapNotNull {
        NodeModuleSearchUtil.resolveModuleFromNodeModulesDir(
          it.parent, NUXT_PKG, NodeModuleDirectorySearchProcessor.PROCESSOR)
      }
      .mapNotNull { it.moduleSourceRoot.findChild("webpack.config.js") }
      .map { it.path }
      .firstOrNull()
}