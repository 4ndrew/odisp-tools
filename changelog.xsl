<?xml version="1.0" encoding="KOI8-R"?>

<xsl:stylesheet
    xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
    version='1.0'>

<!--
 The Apache Software License, Version 1.1

 Copyright (c) 2002 The Apache Software Foundation.  All rights
 reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in
    the documentation and/or other materials provided with the
    distribution.

 3. The end-user documentation included with the redistribution, if
    any, must include the following acknowlegement:
       "This product includes software developed by the
        Apache Software Foundation (http://www.apache.org/)."
    Alternately, this acknowlegement may appear in the software itself,
    if and wherever such third-party acknowlegements normally appear.

 4. The names "Ant" and "Apache Software
    Foundation" must not be used to endorse or promote products derived
    from this software without prior written permission. For written
    permission, please contact apache@apache.org.

 5. Products derived from this software may not be called "Apache"
    nor may "Apache" appear in their names without prior written
    permission of the Apache Group.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 ====================================================================

 This software consists of voluntary contributions made by many
 individuals on behalf of the Apache Software Foundation.  For more
 information on the Apache Software Foundation, please see
 <http://www.apache.org/>.
 -->
  <xsl:param name="title"/>
  <xsl:param name="module"/>
  <xsl:param name="cvsweb"/>

  <xsl:output method="html" indent="yes" encoding="US-ASCII"
              doctype-public="-//W3C//DTD HTML 4.01//EN"
              doctype-system="http://www.w3.org/TR/html401/strict.dtd"/>

  <!-- Copy standard document elements.  Elements that
       should be ignored must be filtered by apply-templates
       tags. -->
  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="attribute::*[. != '']"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="changelog">
    <html>
      <head>
        <title><xsl:value-of select="$title"/></title>
      </head>
      <body>
        <h3>
          <a name="top"><xsl:value-of select="$title"/></a>
        </h3>
        <p style="text-align: right">Based on work done for <a href="http://jakarta.apache.org/ant/">Apache Ant</a>.</p>
        Total commits in this report: <xsl:value-of select="count(//entry)"/>.
        <!-- Timeline: <xsl:call-template name="timelineGen"/> -->
        <hr/>
        <table border="0" width="100%" cellspacing="1">
          
          <xsl:apply-templates select=".//entry">
            <xsl:sort select="date" data-type="text" order="descending"/>
            <xsl:sort select="time" data-type="text" order="descending"/>
          </xsl:apply-templates>
          
        </table>
        
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="entry">
    <tr>
      <td><xsl:apply-templates select="date"/></td>
      <td><b><xsl:value-of select="author"/></b></td>
    </tr>
    <tr>
      <td valign="top"><xsl:apply-templates select="time"/></td>
      <td><xsl:apply-templates select="msg"/></td>
    </tr>
    <tr>
      <td/>
      <td>
        <table border="0">
          <xsl:apply-templates select="file"/>
        </table>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="date">
    <nobr><i><xsl:value-of select="."/></i></nobr>
  </xsl:template>

  <xsl:template match="time">
    <i><xsl:value-of select="."/></i>
  </xsl:template>

  <xsl:template match="author">
    <i>
      <a>
        <xsl:attribute name="href">mailto:<xsl:value-of select="."/></xsl:attribute>
        <xsl:value-of select="."/></a>
    </i>
  </xsl:template>

  <xsl:template match="file">
    <tr>
      <td><xsl:value-of select="name"/></td>
        <xsl:choose>
          <xsl:when test="string-length(prevrevision) &gt; 0 ">
            <td>
            <a><xsl:attribute name="href">
              <xsl:value-of select="$cvsweb"/><xsl:value-of select="$module" />/<xsl:value-of select="name" />?rev=<xsl:value-of select="prevrevision" />&amp;content-type=text/vnd.viewcvs-markup</xsl:attribute>
              <xsl:value-of select="prevrevision"/>
          </a> 
        </td>
      <td>
          <a><xsl:attribute name="href"><xsl:value-of select="$cvsweb"/><xsl:value-of select="$module" />/<xsl:value-of select="name" />?r1=<xsl:value-of select="revision" />&amp;r2=<xsl:value-of select="prevrevision"/></xsl:attribute>-&gt;</a>
        </td>
      <td>

          <a><xsl:attribute name="href"><xsl:value-of select="$cvsweb"/><xsl:value-of select="$module" />/<xsl:value-of select="name" />?rev=<xsl:value-of select="revision" />&amp;content-type=text/vnd.viewcvs-markup</xsl:attribute>
          <xsl:value-of select="revision"/></a>
        </td>
          </xsl:when>
          <xsl:otherwise>
      <td>
<a><xsl:attribute name="href"><xsl:value-of select="$cvsweb"/><xsl:value-of select="$module" />/<xsl:value-of select="name" />?rev=<xsl:value-of select="revision" />&amp;content-type=text/vnd.viewcvs-markup</xsl:attribute>
          <xsl:value-of select="revision"/></a>
        </td>
        <td/>
        <td/>
          </xsl:otherwise>
        </xsl:choose>
    </tr>
  </xsl:template>

  <!-- Any elements within a msg are processed,
       so that we can preserve HTML tags. -->
  <xsl:template match="msg">
    <pre><xsl:apply-templates/></pre>
  </xsl:template>
  
</xsl:stylesheet>
