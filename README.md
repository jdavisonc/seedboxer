## What is SeedBoxer?
SeedBoxer is a distribution content system based on users queues and filters. It was designed to be hosted inside a Seed Box, a remote server used to download and share content through the Internet. 
SeedBoxer was created to work with downloads managers like rtorrent, utorrent, trasmission. This managers need to be configured to use watch, in progress and complete folders.

## How it works?

### Transport
*   FTP

### Post Actions
*   SSH send command

### Notification
*   Email
*   C2DM (Android Push Notification)

## Requirements

*   Java version 1.6+
*   MySQL Server version 5.1+

## Installation

### Create Database

Create a MySQL database with name *seedboxer* and create its schema with the file [seedboxer_schema.sql] (https://github.com/seedboxer/seedboxer/blob/master/sql/seedboxer_schema.sql)

### Deploy

SeedBoxer can be deploy as standalone or inside a Java web application container like Tomcat. If you want to deploy it as web application, simple copy the SeedBoxer WAR file to your Tomcat webapps folder. Otherwise go to the project [SeedBoxer Standalone](https://github.com/seedboxer/seedboxer-standalone) to see further information.

## Configuration
Currently, SeedBoxer only support user configuration via Database.

#### New user
```mysql
INSERT INTO `users` (`username`, `password`) VALUES ('username', MD5('password'));
```

#### User properties
```mysql
INSERT INTO `configurations` (`user_id`, `name`, `value`) VALUES ('id_user', 'prop_name', 'prop_value');
```

Properties names:
*   proeasy_ftp_username
*   proeasy_ftp_password
*   proeasy_ftp_url
*   proeasy_ftp_remoteDir
*   proeasy_notification_email_email
*   proeasy_ssh_cmd
*   proeasy_ssh_url
*   proeasy_ssh_username
*   proeasy_ssh_password
*   proeasy_notification_email
*   proeasy_notification_c2dm

#### New Feed
```mysql
INSERT INTO `feeds` (`url`) VALUES ('http://feedserver/feed.xml');
```

## API

SeedBoxer offers access to that corpus of data, via APIs. Each API represents a facet of SeedBoxer, and allows developers to build upon and extend their applications in new and creative ways. It's important to note that the SeedBoxer APIs are constantly evolving.

SeedBoxer expose its APIs via RESTful services, each API is accesible under the url *http://host/webservices*

#### Status
<table>
  <tr>
    <th>Resource</th><th>Description</th>
  </tr>
  <tr>
    <td>GET webservices/status</td><td></td>
  </tr>
</table>

#### Downloads
<table>
  <tr>
    <th>Resource</th><th>Description</th>
  </tr>
  <tr>
    <td>GET downloads/list</td><td></td>
  </tr>
  <tr>
    <td>GET downloads/put</td><td></td>
  </tr>
  <tr>
    <td>GET downloads/delete</td><td></td>
  </tr>
  <tr>
    <td>GET downloads/queue</td><td></td>
  </tr>
</table>

#### C2DM
<table>
  <tr>
    <th>Resource</th><th>Description</th>
  </tr>
  <tr>
    <td>GET registerDevice</td><td></td>
  </tr>
</table>

#### Torrents
<table>
  <tr>
    <th>Resource</th><th>Description</th>
  </tr>
  <tr>
    <td>GET torrents/add</td><td></td>
  </tr>
</table>


## Authors and Contributors
SeedBoxer is the work of:
*   [Jorge Davison (jdavisonc)](http://github.com/jdavisonc)
*   [Farid Elias (The-Sultan)](http://github.com/the-sultan)
For full list of contributors see the [CONTRIBUTORS]  (https://github.com/seedboxer/seedboxer/blob/master/CONTRIBUTORS) file.

## Support or Contact

If you have any issue or feature request with/for SeedBoxer, please add an issue on [GitHub](https://github.com/seedboxer/seedboxer/issues) or fork the project and send a pull request.


## License

Copyright (c) 2012 SeedBoxer Team.

SeedBoxer is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

SeedBoxer is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
