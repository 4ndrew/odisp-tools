<?xml version='1.0' encoding='koi8-r'?>
<!-- $Id: messages.xsl,v 1.1 2003/10/14 13:46:24 valeks Exp $ -->
<xsl:stylesheet 
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd">
    <xsl:output method="html" encoding="koi8-r"/>
	<xsl:template match="messages">
	    <html>
		<head>
		    <title>Описание используемых сообщений</title>
		</head>
		<body>
		    <table>
			<tr><td>Сообщение</td><td>Описание</td></tr>
			<xsl:apply-templates select="message"/>
		    </table>
		</body>
	    </html>
	</xsl:template>
	<xsl:template match="message">
	    <tr>
		<td><xsl:value-of select="text()"/></td>
		<td><xsl:value-of select="data"/></td>
	    </tr>
	</xsl:template>
</xsl:stylesheet>
	    