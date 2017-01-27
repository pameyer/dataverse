For non-interactive use, the installer can be run with the `./installer -y -f`.
Default configuration values will be read from the `default.config` file (containing key value pairs separated by a tab), if it exists (if not, values hard-coded in the installer script are used).

Needs some more work; ADMIN_EMAIL and HOST_DNS_ADDRESS (possibly others) ignored by installer; MAIL_SERVER correct at least.

Also still needs a keypress for asadmin password.

