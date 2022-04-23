<template>
  <div>
    <v-dialog v-model="booleanDialog" persistent max-width="500px">
      <template v-slot:activator="{ on: dialog, attrs: dialogattr }">
        <v-tooltip dark bottom>
          <template v-slot:activator="{ on: tooltip, attrs: tooltipattr }">
            <v-btn
              dark
              x-small
              text
              :retain-focus="false"
              v-bind="{ ...dialogattr, tooltipattr }"
              v-on="{ ...tooltip, ...dialog }"
              @click="enableDialog(item)"
            >
              <v-icon dark>mdi-pencil</v-icon>
            </v-btn>
          </template>

          <span>Account 정보 수정</span>
        </v-tooltip>
      </template>

      <!-- Form Data -->
      <v-card>
        <v-card-title class="blue-grey lighten-4">
          <span class="text-h6" dark> Account 정보 수정 </span>
        </v-card-title>

        <v-divider></v-divider>

        <v-card-text>
          <v-form ref="form" v-model="valid" @submit.prevent="editAccount()">
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="HostedzoneName"
                  label="Zone Name*"
                  required
                />
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="HostedzoneId"
                  label="Zone Id*"
                  required
                />
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="awsAccessKey"
                  label="awsAccessKey*"
                  required
                />
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="awsCredentialKey"
                  label="awsCredentialKey*"
                  required
                />
              </v-col>
              <v-col cols="6">
                <v-text-field v-model="team" label="당신의 팀은?*" required />
              </v-col>
              <v-col cols="6">
                <v-text-field
                  v-model="awsAccount"
                  label="당신의 AWS 계정 이름은?*"
                  required
                />
              </v-col>
            </v-row>
            <v-card-actions>
              <v-btn color="blue darken-1" nuxt text @click="cancel()">
                취소
              </v-btn>
              <v-btn color="blue darken-1" text type="submit"> 저장 </v-btn>
            </v-card-actions>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      required: true,
    },
  },

  data() {
    return {
      booleanDialog: false,

      // Form Validate
      valid: false,

      // Form Data
      accountIdx: '',
      HostedzoneName: '',
      HostedzoneId: '',
      team: '',
      awsAccount: '',
      awsAccessKey: '',
      awsCredentialKey: '',
    }
  },

  methods: {
    toggleDialog() {
      this.booleanDialog = !this.booleanDialog
    },

    enableDialog(item) {
      try {
        console.log(item)
        this.accountIdx = item.accountIdx
        this.HostedzoneName = item.hostedZoneName
        this.HostedzoneId = item.hostedZoneId
        this.team = item.team
        this.awsAccount = item.accountName
        this.awsAccessKey = item.awsAccessKey
        this.awsCredentialKey = item.awsCredentialKey
      } catch (error) {
      } finally {
        this.toggleDialog()
      }
    },

    initialContents() {
      this.accountIdx = ''
      this.HostedzoneName = ''
      this.HostedzoneId = ''
      this.team = ''
      this.awsAccount = ''
      this.awsAccessKey = ''
      this.awsCredentialKey = ''
    },

    cancel() {
      this.initialContents()
      this.toggleDialog()
    },

    async editAccount() {
      if (this.$refs.form.validate()) {
        try {
          await this.$store.dispatch('account/putAccount', {
            accountIdx: this.accountIdx,
            accountName: this.awsAccount,
            team: this.team,
            hostedZoneName: this.HostedzoneName,
            hostedZoneId: this.HostedzoneId,
            awsAccessKey: this.awsAccessKey,
            awsCredentialKey: this.awsCredentialKey,
          })

          await this.$store.dispatch('account/loadAccountItems')
        } catch (error) {}
        // TODO: Exception PAGE or Alert 만들기!
      }
      await this.initialContents()
      await this.toggleDialog()
    },
  },
}
</script>

<style scoped></style>
