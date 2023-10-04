import javaobj
import subprocess
import os
import xml.etree.ElementTree as ET
import re

XML_FILENAME = 'standalone.xml'
VAULT_FILENAME = 'VAULT.dat'
KEYSTORE_FILENAME = 'jbvault.jks'

def extract_vault_values(xml_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()

    # Get the default namespace from the root's tag
    match = re.match(r'{(.*)}', root.tag)
    ns = match.group(1) if match else ''

    # Find the <vault> element
    vault = root.find(f"{{{ns}}}vault")

    if not vault:
        print("No <vault> element found.")
        return

    vault_options = {}
    for option in vault.findall(f"{{{ns}}}vault-option"):
        name = option.attrib.get('name')
        value = option.attrib.get('value')
        vault_options[name] = value
    
    required_keys = [
        'KEYSTORE_PASSWORD',
        'KEYSTORE_ALIAS',
        'SALT',
        'ITERATION_COUNT'
    ]

    for key in required_keys:
        if key not in vault_options:
            raise ValueError(f"Missing required key: {key}")

    print(f'Successfully loaded values from {xml_file}')
    return vault_options

CWD = os.getcwd()
XML_FILE_PATH = f"{CWD}/inputs/{XML_FILENAME}"

values = extract_vault_values(XML_FILE_PATH)
# Get these values from the standalone.xml file
KEYSTORE_PASSWORD = values['KEYSTORE_PASSWORD']
KEYSTORE_ALIAS = values['KEYSTORE_ALIAS']
SALT = values['SALT']
ITERATION_COUNT = values['ITERATION_COUNT']
# Manually setting these
KEYSTORE_URL = f"{CWD}/inputs/{KEYSTORE_FILENAME}"
ENC_FILE_DIR = f"{CWD}/inputs"
VAULT_FILE_PATH = f"{CWD}/inputs/{VAULT_FILENAME}"

def decrypt_vault_passwords():
 global KEYSTORE_PASSWORD, KEYSTORE_ALIAS, SALT, ITERATION_COUNT, KEYSTORE_URL, ENC_FILE_DIR, VAULT_FILE_PATH
 print('[+] Dumping vault data:')
 print("--------------------")
 jobj = open(VAULT_FILE_PATH,'rb').read()

 pobj = javaobj.loads(jobj)
 for i in range(0, len(pobj.annotations[1].annotations), 2):
  key = pobj.annotations[1].annotations[i]
  value = pobj.annotations[1].annotations[i + 1]
  if key:
    command = f"java --class-path './lib/*' jboss_decrypt_vault.java {ENC_FILE_DIR} {KEYSTORE_URL} {ITERATION_COUNT} {SALT} {KEYSTORE_ALIAS} {KEYSTORE_PASSWORD} {key}"
    result = subprocess.run(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    # Get the output
    output = result.stdout.decode('utf-8').strip()
    print(f"{key},{output}")

decrypt_vault_passwords()
