<?xml version='1.0' encoding='koi8-r'?>
<!-- $Id: messages.xsl,v 1.2 2003/10/14 18:43:46 valeks Exp $ -->
<xsl:stylesheet 
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd">
    <xsl:output method="html" encoding="koi8-r"/>
	<xsl:template match="messages">
	    <html>
		<head>
		    <title>Описание используемых сообщений</title>
		    <style type="text/css">
			//table {border-style:solid; border-width:1;}
			td.bordered {border-style:solid; border-width:1;}			
		    </style>
		</head>
		<body>
		    <table border="0" cellspacing="0">
			<tr><td class="bordered"><b>Сообщение</b></td><td class="bordered"><b>Описание</b></td></tr>
			<xsl:apply-templates select="message"/>
		    </table>
		</body>
	    </html>
	</xsl:template>
	<xsl:template match="message">
	    <tr>
		<td valign="top" class="bordered"><xsl:value-of select="text()"/></td>
		<xsl:choose>
		    <xsl:when test="count(data/*) &gt; 0">
			<td class="bordered"><xsl:apply-templates select="data/*"/></td>
		    </xsl:when>
		    <xsl:otherwise>
			<td class="bordered" bgcolor="red">Описание не найдено</td>
		    </xsl:otherwise>
		</xsl:choose>
	    </tr>
	</xsl:template>
	<xsl:template match="description">
	    <table border="0" cellspacing="0">
		<tr><td colspan="2">Назначение: <xsl:value-of select="purpose/text()"/></td></tr>
		<xsl:if test="count(param) &gt; 0">
		    <tr><td colspan="3">Список параметров</td></tr>
		    <xsl:apply-templates select="param"/>
		</xsl:if>
	    </table>
	</xsl:template>
	<xsl:template match="param">
	    <tr><td>
		<xsl:value-of select="position()"/>
		<xsl:if test="mult &gt; 0">
		    -<xsl:value-of select="position()+mult"/>
		</xsl:if>
	    </td><td><xsl:value-of select="text()"/></td>
		<xsl:choose>
		    <xsl:when test="mandatory = 'yes'">
			<td>обязательный</td>
		    </xsl:when>
		    <xsl:otherwise>
			<td></td>
		    </xsl:otherwise>
		</xsl:choose>
	    </tr>
	</xsl:template>
</xsl:stylesheet>
	    