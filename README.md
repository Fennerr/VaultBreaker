# VaultBreaker

A tool to decrypt values stored in a JBoss vault

## Background

Plaintext values can be removed from a JBoss installation's `standalone.xml`, and stored in a [vault](https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/6.4/html/security_guide/chap-secure_passwords_and_other_sensitive_strings_with_password_vault).
The native tooling provided (`EAP_HOME/bin/vault.sh`) only allows you to modify values in the vault, and not retrieve or view them.
But with access to the `standalone.xml` file, the keystore, and the vault file you can decrypt and retrieve all values inside the vault.

This wasnt the most trivial thing to do, because I'm not great with Java, and the POCs I came across were outdated and no longer worked. Hopefully this tool will work for the next couple of years.


## Setup

### Collect files from server
Place the following 3 files into the **inputs** directory:
* `stanalone.xml` from `/path/to/jboss/standalone/configuration/standalone.xml`.
* Grab the keystore - it can be found from the `KEYSTORE_URL` in the `standalone.xml` file. It is named `jbvault.jks` by default, but the name can be modified.
* Grab the vault - from what I have seen, it is called `VAULT.dat` and stored in the `ENC_FILE_DIR` value in the `standalone.xml` file. 

### Setup a python virtual environment, with dependencies 
Use whatever method you are familar with. Here is an example using `virtualenv`
```
virtualenv -p python3 .venv
source .venv/bin/activate 
pip install -r requirements.txt
```

### Update filenames if required
If your vault is not named `VAULT.dat`, the keystore is not named `jbvault.jks`, or the stanadlone file is not named `standalone.xml` then change lines 7-9 in `vaultbreaker3.py` as required.

## Decrypting the vault contents

Just execute the script after completing the setup:
```
python vaultbreaker3.py
```
The script will pull the appropriate values from the XML file, list all the values inside the vault, and then decrypt them 1-by-1 using `jboss_decrypt_vault.java`.

### Notes
Items in the vault are referenced in `standalone.xml` using the following syntax:
`VAULT::VAULT_BLOCK::ATTRIBUTE_NAME::1`

I have only seen the first value of the attribute (`::1`) being referenced, so this is hardcoded in the logic in `jboss_decrypt_vault.java`

I have provided `jboss_decrypt_vault_hardcoded.java` if you would like to hardcode the exact value you would like to retrieve. You will need to update the various values in the script with the appropriate values from `standalone.xml`, and then provide a reference to the value you would like to retrieve from the vault.

