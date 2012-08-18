SeedBoxer
=========

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
```bash
$ cd your_repo_root/repo_name
```

### Install daemon
```bash
$ cd your_repo_root/repo_name
```

## Configuration

### New user
```mysql
INSERT INTO `users` (`username`, `password`) VALUES ('username', MD5('password'));
```

### User properties
```mysql
INSERT INTO `configurations` (`user_id`, `name`, `value`) VALUES ('id_user', 'prop_name', 'prop_value'),
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

### Content filter

## Authors and Contributors
SeedBoxer is the work of [Jorge Davison (jdavisonc)]((http://github.com/jdavisonc)) and [Farid Elias (The-Sultan)]((http://github.com/the-sultan)), for full list
of contributors see the
[CONTRIBUTORS](https://github.com/seedboxer/seedboxer/blob/master/CONTRIBUTORS) file.

## Support or Contact

If you have any issue or feature request with/for SeedBoxer, please add an issue on [GitHub](https://github.com/seedboxer/seedboxer/issues) or fork the project and send a pull request.


## License

Copyright (c) 2012 SeedBoxer Team.

SeedBoxer is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

SeedBoxer is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
