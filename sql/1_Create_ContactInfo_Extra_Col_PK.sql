USE [instituto]

-- Se elimina la tabla anterior si la hubiese
IF (OBJECT_ID('dbo.[FK_contactInfo_profesor]', 'F') IS NOT NULL)
BEGIN
	ALTER TABLE [dbo].[contactInfo] DROP CONSTRAINT [FK_contactInfo_profesor]
END

IF OBJECT_ID(N'dbo.[contactInfo]', N'U') IS NOT  NULL
BEGIN
	/****** Object:  Table [dbo].[contactInfo]    Script Date: 09/02/2023 12:11:39 ******/
	DROP TABLE [dbo].[contactInfo]	   
END

-- Creamos tabla contactInfo con columna id autoincremental IDENTITY como PK

CREATE TABLE [dbo].[contactInfo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[profesorId] [int] NOT NULL,
	[email] [varchar](255) NOT NULL,
	[tlf_movil] [varchar](15) NULL,
	
 CONSTRAINT [PK_contactInfo_1] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UC_contactInfo_UNIQUE_profesorID] UNIQUE NONCLUSTERED 
(
	[profesorId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[contactInfo]  WITH CHECK ADD  CONSTRAINT [FK_contactInfo_profesor] FOREIGN KEY([profesorId])
REFERENCES [dbo].[profesor] ([Id])
GO

ALTER TABLE [dbo].[contactInfo] CHECK CONSTRAINT [FK_contactInfo_profesor]

ALTER TABLE contactInfo
ADD CONSTRAINT UC_contactInfo_UNIQUE_email UNIQUE(email)

GO


