<template>
  <div>
    <v-card>
      <v-card-title class="d-flex justify-center mb-6">
        Route53 Admin Page
        <v-spacer />
        <v-card-actions>
          <!-- Method: addHostZoneId -->
          <create-account-dialog />

          <!-- Method: loadAccountItems -->
          <div class="pl-3">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  dark
                  small
                  color="info"
                  v-bind="attrs"
                  v-on="on"
                  @click="loadAccountItems"
                >
                  <v-icon>mdi-reload</v-icon>
                </v-btn>
              </template>
              <span>DNS Zone 새로고침</span>
            </v-tooltip>
          </div>
        </v-card-actions>
      </v-card-title>
    </v-card>

    <v-data-table :headers="headers" :items="accountItems" class="elevation-3">
      <template v-slot:body="{ items }">
        <tbody>
          <tr v-for="item in items" :key="item.accountIdx">
            <td>{{ item.accountName }}</td>
            <td>{{ item.team }}</td>
            <td>{{ item.hostedZoneName }}</td>
            <td>{{ item.hostedZoneId }}</td>
            <!-- <td>{{ item.aws_access_key }}</td> -->
            <!-- <td>{{ item.aws_secret_key }}</td> -->
            <td>
              <v-row>
                <UpdateAccountDialog :item="item" />
                <DeleteAccountDialog :item="item" />
              </v-row>
            </td>
          </tr>
        </tbody>
      </template>
    </v-data-table>
    <snackbar :snackbar="snackbar" />
  </div>
</template>

<script>
import CreateAccountDialog from '~/components/account/CreateAccountDialog.vue'
import UpdateAccountDialog from '~/components/account/UpdateAccountDialog.vue'
import DeleteAccountDialog from '~/components/account/DeleteAccountDialog.vue'
import Snackbar from '~/components/common/snackbar.vue'

export default {
  components: {
    CreateAccountDialog,
    UpdateAccountDialog,
    DeleteAccountDialog,
    Snackbar,
  },

  async fetch() {
    await this.$store.dispatch('account/loadAccountItems')
  },

  data() {
    return {
      // autoCompletes
      values: '',
      snackbar: { bool: false, content: '' },

      headers: [
        { text: 'AWS Account', value: 'accountName', width: '20%' },
        { text: 'Team', value: 'team', width: '20%' },
        { text: 'Zone Name', value: 'hostedZoneName', width: '10%' },
        { text: 'Zone Id', value: 'hostedZoneId', width: '30%' },
        // { text: 'aws_access_key', value: 'aws_access_key' },
        // { text: 'aws_secret_key', value: 'aws_secret_key' },
        { text: 'Actions', value: 'actions', width: '20%' },
      ],
    }
  },

  computed: {
    accountItems() {
      return this.$store.state.account.accountItems
    },

    accountItem() {
      return this.$store.state.account.accountItem
    },
  },

  mounted() {
    this.$store.dispatch('account/loadAccountItems')
  },

  methods: {
    async loadAccountItems() {
      await this.$store
        .dispatch('account/loadAccountItems')
        .then((res) => {
          this.snackbar.bool = true
          this.snackbar.content = '계정 새로고침 성공'
        })
        .catch((err) => {
          this.snackbar.bool = true
          this.snackbar.content = '계정 새로고침 실패: ' + err
        })
    },
  },
}
</script>

<style scoped></style>
