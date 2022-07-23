<script>
export default {
  middleware: [
    function ({ redirect }) {
      redirect('/v2/route53')
    },
  ],
}
</script>
