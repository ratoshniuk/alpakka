/*
 * Copyright (C) 2016-2017 Lightbend Inc. <http://www.lightbend.com>
 */

package playground

import java.io.File
import java.net.URI
import java.nio.file.FileSystem

import org.apache.ftpserver.{ConnectionConfigFactory, FtpServer, FtpServerFactory}
import org.apache.ftpserver.listener.ListenerFactory
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory
import playground.filesystem.JimfsFactory

class FtpServerEmbedded {

  val DEFAULT_LISTENER = "default"

  def start(fs: FileSystem, port: Int): FtpServer = {
    val factory = new ListenerFactory()
    factory.setServerAddress("localhost")
    factory.setPort(port)

    val usersFile = new File("ftpusers.properties")
    val pumf = new PropertiesUserManagerFactory
    pumf.setFile(usersFile)
    val userMgr = pumf.createUserManager

    val serverFactory = new FtpServerFactory()
    serverFactory.setUserManager(userMgr)
    serverFactory.setFileSystem(new JimfsFactory(fs))
    serverFactory.setConnectionConfig(new ConnectionConfigFactory().createConnectionConfig)
    serverFactory.addListener(DEFAULT_LISTENER, factory.createListener)

    val ftpServer = serverFactory.createServer()
    ftpServer.start()
    ftpServer
  }

}

object FtpServerEmbedded extends FtpServerEmbedded