#service principle
provider "azurerm" {
subscription_id="6061b278-aa06-4d72-ac52-68e595738a0b"
client_id="29ca510e-f48b-48f8-b0a6-e38d2194d712"
client_secret="5Me7Q~4psonzYrc5bZwsSciKv7IBLo7061qAL"
tenant_id="72815fae-4d5f-4afc-a95d-0397397b3d1a"
features{}
}
#create a resource group if it doesnot exist
resource "azurerm_resource_group" "demorg" {
name = "rg-dev"
location="eastus"
tags={
environment = "Terraform Demo"
}

resource "azurerm_storage_account" "example" {
  name                     = "devstorage"
  resource_group_name      = azurerm_resource_group.demorg.name
  location                 = azurerm_resource_group.demorg.location
  account_tier             = "Standard"
  account_replication_type = "GRS"

  tags = {
    environment = "staging"
  }
}
}