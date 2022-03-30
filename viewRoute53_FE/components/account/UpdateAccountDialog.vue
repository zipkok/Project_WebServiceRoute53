<template>
  <div>
    <v-dialog
      v-model="booleanDialog"
      :retain-focus="false"
      persistent
      max-width="600px"
    >
      <template v-slot:activator="{ on: dialog, attrs: dialogattr }">
        <v-tooltip bottom>
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
              <v-icon color="grey darken-2">mdi-pencil</v-icon>
            </v-btn>
          </template>

          <span>Account 정보 수정</span>
        </v-tooltip>
      </template>

      <!-- Form Data -->
      <v-card>
        <div style="background-color: #fffff0">
          <v-card-title>
            <span class="text-h5"> Account 정보 수정 </span>
          </v-card-title>
        </div>
        <v-divider></v-divider>
        <v-card-text>
          <v-container>
            <v-form ref="form" v-model="valid" @submit.prevent="editItem(item)">
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
            </v-form>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" nuxt text @click="cancel()">
            취소
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            type="submit"
            @click="saveAccount()"
          >
            저장
          </v-btn>
        </v-card-actions>
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
        this.HostedzoneName = this.item.hostedZoneName
        this.HostedzoneId = item.hostedZoneId
        this.team = item.team
        this.awsAccount = item.accountName
        this.awsAccessKey = item.aws_access_key
        this.awsCredentialKey = item.aws_secret_key
      } catch (error) {
      } finally {
        this.toggleDialog()
      }
    },

    initialContents() {
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

    async saveAccount(items) {
      // TODO: Validattion 조건 만들어야한다.
      if (this.$refs.form.validate()) {
        await this.$store
          .dispatch('account/putAccount', {
            hostedZoneName: this.HostedzoneName,
            hostedZoneId: this.HostedzoneId,
            team: this.team,
            accountName: this.awsAccount,
            awsAccessKey: this.awsAccessKey,
            awsCredentialKey: this.awsCredentialKey,
          })
          .then((res) => {
            this.$store.dispatch('account/loadAccountItems')
          })
          .catch(() => {
            // TODO: Exception PAGE or Alert 만들기!
          })
      }

      await this.initialContents()
      await this.toggleDialog()
    },

    async loadAccountByAccountItems(items) {
      await this.$store.dispatch('account/loadAccountByAccountItems')
    },

    editItem(value) {},
  },
}
</script>

<style scoped></style>
