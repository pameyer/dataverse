For non-interactive use, the installer can be run with the `./installer -y -f`.
Default configuration values will be read from the `default.config` file (containing key value pairs separated by a tab), if it exists (if not, values hard-coded in the installer script are used).

Needs some more work; HOST_DNS_ADDRESS (possibly others) ignored by installer; MAIL_SERVER correct at least.

The email address for the default "dataverseAdmin" account can be changed by editing `scripts/api/data/user-admin.json` (aka - not directly accessable through the installer script, and not API editable).

